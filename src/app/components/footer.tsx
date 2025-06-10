"use client"

import type React from "react"

import { useState } from "react"

export default function Footer() {
  const [email, setEmail] = useState("")

  const handleNewsletterSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    console.log("Newsletter subscription:", email)
    alert("Inscrição realizada com sucesso!")
    setEmail("")
  }

  const scrollToSection = (sectionId: string) => {
    const element = document.getElementById(sectionId)
    if (element) {
      element.scrollIntoView({ behavior: "smooth" })
    }
  }

  return (
    <footer className="footer">
      <div className="footer-top">
        <div className="container">
          <div className="footer-grid">
            <div className="footer-info">
              <div className="footer-logo">
                <span className="logo-icon">
                  <i className="fas fa-route"></i>
                </span>
                <span className="logo-text">EcoRota</span>
              </div>
              <p>Tecnologia e inteligência para uma cidade mais limpa e eficiente.</p>
              <div className="social-links">
                <a href="#" className="social-link">
                  <i className="fab fa-linkedin-in"></i>
                </a>
                <a href="#" className="social-link">
                  <i className="fab fa-twitter"></i>
                </a>
                <a href="#" className="social-link">
                  <i className="fab fa-facebook-f"></i>
                </a>
                <a href="#" className="social-link">
                  <i className="fab fa-instagram"></i>
                </a>
              </div>
            </div>
            <div className="footer-links">
              <h3>Links Rápidos</h3>
              <ul>
                <li>
                  <a
                    href="#"
                    onClick={(e) => {
                      e.preventDefault()
                      scrollToSection("home")
                    }}
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
                  >
                    Contato
                  </a>
                </li>
              </ul>
            </div>
            <div className="footer-features">
              <h3>Recursos</h3>
              <ul>
                <li>
                  <a
                    href="#"
                    onClick={(e) => {
                      e.preventDefault()
                      scrollToSection("features")
                    }}
                  >
                    Otimização de Rotas
                  </a>
                </li>
                <li>
                  <a
                    href="#"
                    onClick={(e) => {
                      e.preventDefault()
                      scrollToSection("features")
                    }}
                  >
                    Rastreamento em Tempo Real
                  </a>
                </li>
                <li>
                  <a
                    href="#"
                    onClick={(e) => {
                      e.preventDefault()
                      scrollToSection("features")
                    }}
                  >
                    Gestão de Dados
                  </a>
                </li>
                <li>
                  <a
                    href="#"
                    onClick={(e) => {
                      e.preventDefault()
                      scrollToSection("features")
                    }}
                  >
                    Planejamento Inteligente
                  </a>
                </li>
              </ul>
            </div>
            {/* <div className="footer-newsletter">
              <h3>Newsletter</h3>
              <p>Inscreva-se para receber novidades sobre o EchoRota</p>
              <form className="newsletter-form" onSubmit={handleNewsletterSubmit}>
                <input
                  type="email"
                  placeholder="Seu email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                />
                <button type="submit">
                  <i className="fas fa-paper-plane"></i>
                </button>
              </form>
            </div> */}
          </div>
        </div>
      </div>
      <div className="footer-bottom">
        <div className="container">
          <p>
            &copy; <span id="year">2024</span> EcoRota. Todos os direitos reservados.
          </p>
        </div>
      </div>
    </footer>
  )
}
