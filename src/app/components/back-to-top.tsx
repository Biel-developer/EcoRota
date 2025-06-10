"use client"

import { useEffect, useState } from "react"

export default function BackToTop() {
  const [isVisible, setIsVisible] = useState(false)

  useEffect(() => {
    const handleScroll = () => {
      setIsVisible(window.scrollY > 300)
    }

    window.addEventListener("scroll", handleScroll)
    return () => window.removeEventListener("scroll", handleScroll)
  }, [])

  const scrollToTop = () => {
    window.scrollTo({
      top: 0,
      behavior: "smooth",
    })
  }

  return (
    <a
      href="#"
      className={`back-to-top ${isVisible ? "active" : ""}`}
      onClick={(e) => {
        e.preventDefault()
        scrollToTop()
      }}
    >
      <i className="fas fa-arrow-up"></i>
    </a>
  )
}
