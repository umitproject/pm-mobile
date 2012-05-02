package com.test;

public class Parser {
	String[] parsed_packet;
	String line ;
	public Parser(String line){
		this.line=line;
		if (line!=null){
			if(line.charAt(0)=='#')
			{
				System.out.println("charAt(0) is #");
				parsed_packet=null;
			}
			else
			{
				System.out.println("Parsing Packet");
				parsed_packet=line.split("\\|");
				System.out.println("Parsed Packet: ");
				for(int i=0;i<parsed_packet.length;i++)
					System.out.println("parsed_packet["+i+"]= " + parsed_packet[i]);
				
			}
		}
		else
		{
			System.out.println("line read is null");
			parsed_packet=null;
		}
	}
	
	public String header(){
		System.out.println("Finding header for packet: "+ this.line);
		if(parsed_packet[0]==null || parsed_packet[0].charAt(1)=='#'  )
			return null;
		return parsed_packet[0];
		
	}
	public String protocol(){
		System.out.println("Finding protocol for packet: "+ this.line);
		if(parsed_packet[0]==null || parsed_packet[1].charAt(1)=='#')
			return null;
		return parsed_packet[1];
	}
	public String payload(){
		System.out.println("Finding payload for packet: "+ this.line);
		if(parsed_packet[2].charAt(1)=='#' || parsed_packet[2]==null)
			return null;
		return parsed_packet[2];
	}
	

	

}
