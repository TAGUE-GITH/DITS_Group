import Button from "../ui/Button";

export default function CtaSection() {
  return (
    <section className="section" style={{ paddingTop: 0 }}>
      <div className="container">
        <div className="cta">
          <h2>Un projet en tête ?</h2>
          <p>
            Parlons-en ensemble : nous étudions votre besoin et revenons vers
            vous rapidement.
          </p>
          <Button to="/contact" variant="dark">
            Demander un devis gratuit
          </Button>
        </div>
      </div>
    </section>
  );
}