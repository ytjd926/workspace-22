package com.bright.examples.model;

public class BeaconModel {
	
	public String name;
	public String mac;
	public int x;
	public int y;
	public double distance;
	
	public BeaconModel(String name,int x,int y){
		this.name=name;
		this.x=x;
		this.y=y;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	
	
	
}
