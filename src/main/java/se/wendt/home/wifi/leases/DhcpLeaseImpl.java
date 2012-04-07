package se.wendt.home.wifi.leases;

public class DhcpLeaseImpl implements DhcpLease {

	private final String expiration;
	private final String hostname;
	private final String ipaddress;
	private final String macaddress;

	public DhcpLeaseImpl(String hostname, String ipaddress, String macaddress, String expiration) {
		this.hostname = hostname;
		this.ipaddress = ipaddress;
		this.macaddress = macaddress;
		this.expiration = expiration;
	}

	@Override
	public String getClientHostName() {
		return hostname;
	}

	@Override
	public String getIpAddress() {
		return ipaddress;
	}

	@Override
	public String getMacAddress() {
		return macaddress;
	}

	@Override
	public String getExpiration() {
		return expiration;
	}

	@Override
	public int compareTo(DhcpLease o) {
		return this.macaddress.compareTo(o.getMacAddress());
	}

}
