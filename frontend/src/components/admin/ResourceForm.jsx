import { useState } from "react";
import useForm from "../../hooks/useForm";
import useSubmit from "../../hooks/useSubmit";
import { toAssetUrl } from "../../api/http";
import Field from "../ui/Field";
import Button from "../ui/Button";
import Alert from "../ui/Alert";

const initialValues = (fields, item) =>
  Object.fromEntries(
    fields
      .filter(({ type }) => type !== "file")
      .map(({ name, type, default: fallback }) => {
        const raw = item ? item[name] : fallback;

        if (type === "checkbox") return [name, Boolean(raw)];
        if (raw === null || raw === undefined) return [name, ""];
        if (type === "date") return [name, String(raw).slice(0, 10)];
        if (type === "datetime-local") return [name, String(raw).slice(0, 16)];
        return [name, raw];
      })
  );

const toPayload = (values) =>
  Object.fromEntries(
    Object.entries(values).map(([key, value]) => [key, value === "" ? null : value])
  );

const toFormData = (fields, values, files) => {
  const formData = new FormData();

  fields.forEach(({ name, type }) => {
    if (type === "file") {
      if (files[name]) formData.append(name, files[name]);
      return;
    }

    formData.append(name, type === "checkbox" ? Boolean(values[name]) : values[name] ?? "");
  });

  return formData;
};

const renderControl = (field, values, handleChange, item, files, handleFile) => {
  const { name, label, type = "text", options, ...rest } = field;

  if (type === "checkbox") {
    return (
      <label className="check">
        <input type="checkbox" name={name} checked={values[name]} onChange={handleChange} />
        {label}
      </label>
    );
  }

  if (type === "file") {
    const preview = files[name] ? URL.createObjectURL(files[name]) : toAssetUrl(item?.[name]);

    return (
      <div className="file-field">
        <Field
          label={label}
          id={name}
          type="file"
          accept={rest.accept || "image/*"}
          onChange={(event) => handleFile(name, event.target.files[0] || null)}
        />
        {preview && <img className="file-preview" src={preview} alt="" />}
      </div>
    );
  }

  if (type === "select") {
    return (
      <Field label={label} id={name} as="select" value={values[name]} onChange={handleChange} required={rest.required}>
        {options.map(([value, text]) => (
          <option key={value} value={value}>
            {text}
          </option>
        ))}
      </Field>
    );
  }

  return (
    <Field
      label={label}
      id={name}
      as={type === "textarea" ? "textarea" : "input"}
      type={type === "textarea" ? undefined : type}
      value={values[name]}
      onChange={handleChange}
      required={rest.required}
      placeholder={rest.placeholder}
      maxLength={rest.maxLength}
    />
  );
};

export default function ResourceForm({ fields, item, onSubmit, onCancel }) {
  const { values, handleChange } = useForm(initialValues(fields, item));
  const [files, setFiles] = useState({});

  const handleFile = (name, file) => setFiles((current) => ({ ...current, [name]: file }));
  const hasFileField = fields.some(({ type }) => type === "file");

  const { submit, loading, error } = useSubmit(() => {
    if (!hasFileField) return onSubmit(toPayload(values));

    const missing = fields.find(
      ({ name, type, requiredOnCreate }) => type === "file" && requiredOnCreate && !item && !files[name]
    );

    if (missing) throw new Error(`Choisissez ${missing.label.toLowerCase()}.`);

    return onSubmit(toFormData(fields, values, files));
  });

  return (
    <form onSubmit={submit}>
      <Alert>{error}</Alert>
      <div className="form-grid">
        {fields.map((field) => (
          <div key={field.name} className={field.half ? "" : "form-full"}>
            {renderControl(field, values, handleChange, item, files, handleFile)}
          </div>
        ))}
      </div>
      <div className="form-actions">
        <Button type="button" variant="outline" onClick={onCancel}>
          Annuler
        </Button>
        <Button type="submit" disabled={loading}>
          {loading ? "Enregistrement..." : "Enregistrer"}
        </Button>
      </div>
    </form>
  );
}