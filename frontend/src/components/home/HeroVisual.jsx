const layers = [250, 205, 160];

const chips = ["PostgreSQL", "MySQL", "Couchbase"];

function Cylinder({ y }) {
  return (
    <g>
      <path d={`M180 ${y}v34a80 22 0 0 0 160 0v-34z`} fill="url(#dbBody)" />
      <ellipse cx="260" cy={y} rx="80" ry="22" fill="url(#dbTop)" />
      <circle cx="205" cy={y + 22} r="4" fill="#c9a227" />
      <circle cx="220" cy={y + 22} r="4" fill="#ffffff" opacity="0.5" />
    </g>
  );
}

export default function HeroVisual() {
  return (
    <div className="hero-visual">
      <svg className="hero-svg" viewBox="0 0 520 400" fill="none" aria-hidden="true">
        <defs>
          <linearGradient id="dbBody" x1="0" x2="1" y1="0" y2="0">
            <stop offset="0" stopColor="#1d3a66" />
            <stop offset="0.5" stopColor="#2a4f87" />
            <stop offset="1" stopColor="#173157" />
          </linearGradient>
          <linearGradient id="dbTop" x1="0" y1="0" x2="1" y2="1">
            <stop offset="0" stopColor="#e9c95a" />
            <stop offset="1" stopColor="#c9a227" />
          </linearGradient>
          <radialGradient id="glow">
            <stop offset="0" stopColor="#c9a227" stopOpacity="0.35" />
            <stop offset="1" stopColor="#c9a227" stopOpacity="0" />
          </radialGradient>
        </defs>

        <circle cx="260" cy="230" r="220" fill="url(#glow)" />
        <ellipse
          cx="260"
          cy="260"
          rx="215"
          ry="80"
          stroke="rgba(255,255,255,0.14)"
          strokeDasharray="4 6"
        />
        <ellipse cx="260" cy="306" rx="120" ry="26" fill="rgba(201,162,39,0.14)" />

        <path className="flow" d="M260 108V138" stroke="#c9a227" strokeWidth="2.5" />
        <path
          className="flow"
          d="M104 290C140 290 150 255 180 255"
          stroke="#c9a227"
          strokeWidth="2.5"
        />
        <path
          className="flow"
          d="M422 300C385 300 375 255 340 255"
          stroke="#c9a227"
          strokeWidth="2.5"
        />

        {layers.map((y) => (
          <Cylinder key={y} y={y} />
        ))}

        <g className="hero-float">
          <g fill="#ffffff">
            <circle cx="232" cy="82" r="20" />
            <circle cx="262" cy="68" r="27" />
            <circle cx="292" cy="82" r="20" />
            <rect x="232" y="82" width="60" height="20" rx="10" />
          </g>
          <text className="hero-label" x="262" y="28">
            Cloud
          </text>
        </g>

        <g transform="translate(20 235)">
          <rect
            width="84"
            height="110"
            rx="14"
            fill="rgba(255,255,255,0.08)"
            stroke="rgba(255,255,255,0.3)"
          />
          {[16, 44, 72].map((top) => (
            <g key={top}>
              <rect x="14" y={top} width="56" height="20" rx="6" fill="rgba(255,255,255,0.16)" />
              <circle cx="60" cy={top + 10} r="3" fill="#c9a227" />
            </g>
          ))}
          <text className="hero-label" x="42" y="134">
            On-premise
          </text>
        </g>

        <g transform="translate(420 260)">
          <circle
            cx="40"
            cy="40"
            r="38"
            fill="rgba(255,255,255,0.08)"
            stroke="rgba(255,255,255,0.3)"
          />
          <ellipse cx="40" cy="40" rx="16" ry="38" stroke="rgba(255,255,255,0.3)" />
          <path d="M2 40h76M8 20h64M8 60h64" stroke="rgba(255,255,255,0.3)" />
          <text className="hero-label" x="40" y="112">
            International
          </text>
        </g>
      </svg>

      <div className="hero-chips">
        {chips.map((chip) => (
          <span key={chip}>{chip}</span>
        ))}
      </div>
    </div>
  );
}