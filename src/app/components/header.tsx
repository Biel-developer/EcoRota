"use client"
import { useRouter } from "next/navigation"

import { useEffect, useState } from "react"

export default function Header() {
  const [scrolled, setScrolled] = useState(false)
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false)
  const router = useRouter()

  const handleEntrarClick = () => {
    router.push("/login") 
  }

  useEffect(() => {
    const handleScroll = () => {
      setScrolled(window.scrollY > 50)
    }

    window.addEventListener("scroll", handleScroll)
    return () => window.removeEventListener("scroll", handleScroll)
  }, [])

  const scrollToSection = (sectionId: string) => {
    const element = document.getElementById(sectionId)
    if (element) {
      element.scrollIntoView({ behavior: "smooth" })
    }
    setMobileMenuOpen(false)
  }

  return (
    <>
      <header className={`header ${scrolled ? "scrolled" : ""}`} id="header">
        <div className="container">
          <div className="header-content">
            <div className="logo">
              <a
                href="#"
                onClick={(e) => {
                  e.preventDefault()
                  scrollToSection("home")
                }}
              >
                <span className="logo-icon">
                  <i className="fas fa-route"></i>
                </span>
                <span className="logo-text">EcoRota</span>
              </a>
            </div>
            <nav className="nav-menu">
              <ul className="nav-list">
                <li>
                  <a
                    href="#"
                    onClick={(e) => {
                      e.preventDefault()
                      scrollToSection("home")
                    }}
                    className="nav-link active"
                  >
                    Início
                  </a>
                </li>
                <li>
                  <a
                    href="#"
                    onClick={(e) => {
                      e.preventDefault()
                      scrollToSection("about")
                    }}
                    className="nav-link"
                  >
                    Sobre
                  </a>
                </li>
                <li>
                  <a
                    href="#"
                    onClick={(e) => {
                      e.preventDefault()
                      scrollToSection("features")
                    }}
                    className="nav-link"
                  >
                    Recursos
                  </a>
                </li>
                <li>
                  <a
                    href="#"
                    onClick={(e) => {
                      e.preventDefault()
                      scrollToSection("benefits")
                    }}
                    className="nav-link"
                  >
                    Benefícios
                  </a>
                </li>
                <li>
                  <a
                    href="#"
                    onClick={(e) => {
                      e.preventDefault()
                      scrollToSection("contact")
                    }}
                    className="nav-link"
                  >
                    Contato
                  </a>
                </li>
              </ul>
            </nav>
            <div className="header-btn">
              <button onClick={handleEntrarClick} className="btn btn-primary">
                Entrar
              </button>
            </div>
            <div className="mobile-toggle" onClick={() => setMobileMenuOpen(true)}>
              <span></span>
              <span></span>
              <span></span>
            </div>
          </div>
        </div>
      </header>

      <div className={`mobile-menu ${mobileMenuOpen ? "active" : ""}`}>
        <div className="close-menu" onClick={() => setMobileMenuOpen(false)}>
          <i className="fas fa-times"></i>
        </div>
        <ul className="mobile-nav">
          <li>
            <a
              href="#"
              onClick={(e) => {
                e.preventDefault()
                scrollToSection("home")
              }}
              className="mobile-link"
            >
              Início
            </a>
          </li>
          <li>
            <a
              href="#"
              onClick={(e) => {
                e.preventDefault()
                scrollToSection("about")
              }}
              className="mobile-link"
            >
              Sobre
            </a>
          </li>
          <li>
            <a
              href="#"
              onClick={(e) => {
                e.preventDefault()
                scrollToSection("features")
              }}
              className="mobile-link"
            >
              Recursos
            </a>
          </li>
          <li>
            <a
              href="#"
              onClick={(e) => {
                e.preventDefault()
                scrollToSection("benefits")
              }}
              className="mobile-link"
            >
              Benefícios
            </a>
          </li>
          <li>
            <a
              href="#"
              onClick={(e) => {
                e.preventDefault()
                scrollToSection("contact")
              }}
              className="mobile-link"
            >
              Contato
            </a>
          </li>
          <li className="mobile-btn">
            <a
              href="#"
              onClick={(e) => {
                e.preventDefault()
                scrollToSection("contact")
              }}
              className="btn btn-primary"
            >
              Solicitar Demonstração
            </a>
          </li>
        </ul>
      </div>
    </>
  )
}
