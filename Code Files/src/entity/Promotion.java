package entity;

import java.time.LocalDate;

public class Promotion {
	private String title;
	private String description;
	private LocalDate datePosted;
	
	public Promotion(String title, String description, LocalDate datePosted) {
		this.title = title;
		this.description = description;
		this.datePosted = datePosted;
	}
	
	public String getTitle() {return title;}
	public String getDescription() {return description;}
	public LocalDate getDatePosted() {return datePosted;}
}
