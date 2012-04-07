package se.wendt.home.wifi.leases;

public interface DhcpLease extends Comparable<DhcpLease> {

	String getClientHostName();

	String getIpAddress();

	String getMacAddress();

	String getExpiration();

}
