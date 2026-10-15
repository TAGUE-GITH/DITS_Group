import { toAssetUrl } from "../../api/http";

export default function TitleCell({ image, title, sub }) {
  return (
    <div className="thumb-cell">
      {image !== undefined &&
        (image ? (
          <img className="thumb" src={toAssetUrl(image)} alt="" />
        ) : (
          <span className="thumb" />
        ))}
      <div>
        <span className="cell-title">{title}</span>
        {sub && <span className="cell-sub">{sub}</span>}
      </div>
    </div>
  );
}