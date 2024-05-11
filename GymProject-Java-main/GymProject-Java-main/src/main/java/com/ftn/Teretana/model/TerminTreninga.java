package com.ftn.Teretana.model;

import java.time.LocalDateTime;

public class TerminTreninga {
	
	private Long id;
	private Sala sala;
	private Trening trening;
	private LocalDateTime datum;
	private int kapacitet;
	
	public TerminTreninga() {
	}
	
	

	public TerminTreninga(Long id, Trening trening, LocalDateTime datum, int kapacitet) {
		super();
		this.id = id;
		this.trening = trening;
		this.datum = datum;
		this.kapacitet = kapacitet;
	}



	public TerminTreninga(Long id, LocalDateTime datum) {
		super();
		this.id = id;
		this.datum = datum;
	}



	public TerminTreninga(Sala sala, Trening trening, LocalDateTime datum, int kapacitet) {
		super();
		this.sala = sala;
		this.trening = trening;
		this.datum = datum;
		this.kapacitet = kapacitet;
	}

	public TerminTreninga(Long id, Sala sala, Trening trening, LocalDateTime datum, int kapacitet) {
		super();
		this.id = id;
		this.sala = sala;
		this.trening = trening;
		this.datum = datum;
		this.kapacitet = kapacitet;
	}
	
	
	
	public int getKapacitet() {
		return kapacitet;
	}



	public void setKapacitet(int kapacitet) {
		this.kapacitet = kapacitet;
	}



	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Sala getSala() {
		return sala;
	}
	public void setSala(Sala sala) {
		this.sala = sala;
	}
	public Trening getTrening() {
		return trening;
	}
	public void setTrening(Trening trening) {
		this.trening = trening;
	}
	public LocalDateTime getDatum() {
		return datum;
	}
	public void setDatum(LocalDateTime datum) {
		this.datum = datum;
	}
	
	

}
