"use client"

export default function Hero() {
  const scrollToSection = (sectionId: string) => {
    const element = document.getElementById(sectionId)
    if (element) {
      element.scrollIntoView({ behavior: "smooth" })
    }
  }

  return (
    <section className="hero" id="home">
      <div className="hero-bg">
        <video autoPlay muted loop playsInline className="video-bg">
         <source src="/video/videoplayback.mp4" type="video/mp4" />
        </video>
        <div className="overlay"></div>
      </div>
      <div className="container">
        <div className="hero-content">
          <div className="hero-marker">
            <div className="marker-icon">
              <i className="fas fa-truck"></i>
            </div>
          </div>
          <h1 className="hero-title" data-aos="fade-up">
            EcoRota
          </h1>
          <p className="hero-description" data-aos="fade-up" data-aos-delay="200">
            Este sistema tem como objetivo otimizar as rotas de coleta de lixo, tornando o processo mais eficiente,
            econômico e sustentável.
          </p>
          <div className="hero-buttons" data-aos="fade-up" data-aos-delay="400">
            <a
              href="#"
              onClick={(e) => {
                e.preventDefault()
                scrollToSection("about")
              }}
              className="btn btn-primary"
            >
              Saiba Mais
            </a>
            <a
              href="#"
              onClick={(e) => {
                e.preventDefault()
                scrollToSection("contact")
              }}
              className="btn btn-outline"
            >
              Solicitar Demonstração
            </a>
          </div>
        </div>
      </div>
      <div className="scroll-down">
        <a
          href="#"
          onClick={(e) => {
            e.preventDefault()
            scrollToSection("about")
          }}
        >
          <span className="mouse">
            <span className="wheel"></span>
          </span>
        </a>
      </div>
    </section>
  )
}
