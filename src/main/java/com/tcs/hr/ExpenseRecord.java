package com.tcs.hr;

import java.sql.Date;
import java.sql.Time;

public class ExpenseRecord {

	
	private String empid;
	private String username;
	private String travel_route;
	private Date date;
	private Time timeto;
	private Time timefrom;
	private String purpose;
	private String project;
	private double expenseincurred;
	private double advancetaken;
	private String mode;
	private String ticketNo;
	private Date ticketdate;
	private String attachment;
	private String status;
	private int id;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEmpid() {
		return empid;
	}
	public void setEmpid(String empid) {
		this.empid = empid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTravel_route() {
		return travel_route;
	}
	public void setTravel_route(String travel_route) {
		this.travel_route = travel_route;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Time getTimeto() {
		return timeto;
	}
	public void setTimeto(Time timeto) {
		this.timeto = timeto;
	}
	public Time getTimefrom() {
		return timefrom;
	}
	public void setTimefrom(Time timefrom) {
		this.timefrom = timefrom;
	}
	public Date getTicketdate() {
		return ticketdate;
	}
	public void setTicketdate(Date ticketdate) {
		this.ticketdate = ticketdate;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public double getExpenseincurred() {
		return expenseincurred;
	}
	public void setExpenseincurred(double expenseincurred) {
		this.expenseincurred = expenseincurred;
	}
	public double getAdvancetaken() {
		return advancetaken;
	}
	public void setAdvancetaken(double advancetaken) {
		this.advancetaken = advancetaken;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getTicketNo() {
		return ticketNo;
	}
	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	
}
