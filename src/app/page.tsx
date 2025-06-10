"use client"

import { useEffect, useState } from "react"
import Header from "./components/header";
import Hero from "./components/hero";
import About from "./components/about";
import ProblemSolution from "./components/problem-solution";
import Features from "./components/features";
import HowItWorks from "./components/how-it-works";
import Benefits from "./components/benefits";
import Technologies from "./components/technologies";
import Contact from "./components/contact";
import Footer from "./components/footer";
import BackToTop from "./components/back-to-top";
import Preloader from "./components/preloader";

export default function Home() {
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    // Simulate loading time
    const timer = setTimeout(() => {
      setLoading(false)
    }, 2000)

    return () => clearTimeout(timer)
  }, [])

  useEffect(() => {
    // Set current year in footer
    const yearElement = document.getElementById("year")
    if (yearElement) {
      yearElement.textContent = new Date().getFullYear().toString()
    }
  }, [loading])

  if (loading) {
    return <Preloader />
  }

  return (
    <>
      <Header />
      <Hero />
      <About />
      <ProblemSolution />
      <Features />
      <HowItWorks />
      <Benefits />
      <Technologies />
      <Contact />
      <Footer />
      <BackToTop />
    </>
  )
}
