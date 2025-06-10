"use client"

export default function CTA() {
  const scrollToSection = (sectionId: string) => {
    const element = document.getElementById(sectionId)
    if (element) {
      element.scrollIntoView({ behavior: "smooth" })
    }
  }

  return (
    <section className="section cta">
      <div className="container">
        <div className="cta-content" data-aos="fade-up">
          <h2>Pronto para transformar a coleta de resíduos em sua cidade?</h2>
          <p>
            Entre em contato conosco para saber como podemos ajudar a otimizar as rotas de coleta de lixo em seu
            município.
          </p>
          <div className="cta-buttons">
            <a
              href="#"
              onClick={(e) => {
                e.preventDefault()
                scrollToSection("contact")
              }}
              className="btn btn-light"
            >
              Solicitar Demonstração
            </a>
            <a
              href="#"
              onClick={(e) => {
                e.preventDefault()
                scrollToSection("about")
              }}
              className="btn btn-outline-light"
            >
              Saiba Mais
            </a>
          </div>
        </div>
      </div>
    </section>
  )
}
