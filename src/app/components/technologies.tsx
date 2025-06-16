export default function Technologies() {
  return (
    <section className="section technologies">
      <div className="container">
        <div className="section-header">
          <span className="section-tag">Tecnologias</span>
          <h2 className="section-title">Ferramentas que utilizamos</h2>
          <p className="section-subtitle">Combinando o melhor da tecnologia para resultados excepcionais</p>
        </div>
        <div className="tech-grid">
          <div className="tech-card" data-aos="flip-left">
            <div className="tech-icon"><i className="fab fa-react"></i></div>
            <h3>Next.js</h3>
          </div>

          <div className="tech-card" data-aos="flip-left">
            <div className="tech-icon"><i className="fas fa-database"></i></div>
            <h3>PostgreSQL</h3>
          </div>

          <div className="tech-card" data-aos="flip-left">
            <div className="tech-icon"><i className="fab fa-java"></i></div>
            <h3>Java</h3>
          </div>

          <div className="tech-card" data-aos="flip-left">
            <div className="tech-icon"><i className="fas fa-paper-plane"></i></div>
            <h3>Postman</h3>
          </div>
        </div>
      </div>
    </section>
  )
}
