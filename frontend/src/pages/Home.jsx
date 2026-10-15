import Hero from "../components/home/Hero";
import About from "../components/home/About";
import Expertise from "../components/home/Expertise";
import ServicesSection from "../components/home/ServicesSection";
import ProjectsSection from "../components/home/ProjectsSection";
import ArticlesSection from "../components/home/ArticlesSection";
import PartnersSection from "../components/home/PartnersSection";
import CtaSection from "../components/home/CtaSection";
import usePageTitle from "../hooks/usePageTitle";
import "./Home.css";
import ReviewsSection from "../components/home/ReviewsSection";

export default function Home() {
  usePageTitle("Accueil", "DITS Group, consulting et expertise en systèmes d'information, cloud et bases de données.");

  return (
    <>
      <Hero />
      <About />
      <Expertise />
      <ServicesSection />
      <ProjectsSection />
      <ArticlesSection />
      <PartnersSection />
      <CtaSection />
      <ReviewsSection />
    </>
  );
}