const shapes = {
  login: (
    <>
      <path
        d="M200 62l82 30v58c0 56-36 92-82 112-46-20-82-56-82-112V92z"
        fill="#c9a227"
      />
      <path
        d="M162 160l28 28 48-54"
        stroke="#0b1f3a"
        strokeWidth="14"
        strokeLinecap="round"
        strokeLinejoin="round"
      />
    </>
  ),
  register: (
    <>
      <circle cx="195" cy="120" r="46" fill="#c9a227" />
      <path d="M108 250c0-50 40-82 87-82s87 32 87 82z" fill="#c9a227" />
      <circle cx="292" cy="92" r="32" fill="#ffffff" />
      <path
        d="M292 74v36M274 92h36"
        stroke="#0b1f3a"
        strokeWidth="10"
        strokeLinecap="round"
      />
    </>
  ),
  lock: (
    <>
      <path
        d="M162 152v-30a38 38 0 0 1 76 0v30"
        stroke="#ffffff"
        strokeWidth="14"
        strokeLinecap="round"
      />
      <rect x="132" y="150" width="136" height="104" rx="20" fill="#c9a227" />
      <circle cx="200" cy="198" r="13" fill="#0b1f3a" />
      <rect x="194" y="204" width="12" height="28" rx="6" fill="#0b1f3a" />
    </>
  ),
};

export default function AuthIllustration({ variant = "login" }) {
  return (
    <svg
      className="auth-illustration"
      viewBox="0 0 400 320"
      fill="none"
      aria-hidden="true"
    >
      <circle cx="200" cy="160" r="140" fill="rgba(255,255,255,0.05)" />
      <circle cx="200" cy="160" r="98" fill="rgba(255,255,255,0.08)" />
      <circle cx="58" cy="70" r="8" fill="#c9a227" />
      <circle cx="345" cy="245" r="12" fill="rgba(201,162,39,0.6)" />
      <circle cx="332" cy="66" r="6" fill="#ffffff" opacity="0.6" />
      {shapes[variant]}
    </svg>
  );
}