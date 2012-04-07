package se.wendt.home.wifi.leases;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import se.wendt.home.config.ConfigurationManager;
import se.wendt.home.util.LogEnabledBase;

public class LinksysReader extends LogEnabledBase implements DhcpClients {

	public static final String CONFIG_HOST = "linksys.host";
	public static final String CONFIG_PATH = "linksys.path";
	public static final String CONFIG_USERNAME = "linksys.username";
	public static final String CONFIG_PASSWORD = "linksys.password";
	private DefaultHttpClient httpclient;
	private final String host;
	private final String pathPart;

	public LinksysReader(ConfigurationManager config) {
		this(config.getValue(CONFIG_HOST), config.getValue(CONFIG_PATH), config.getValue(CONFIG_USERNAME), config
				.getValue(CONFIG_PASSWORD));
	}

	public LinksysReader(String host, String pathPart, String username, String password) {
		this.host = host;
		this.pathPart = pathPart;
		httpclient = new DefaultHttpClient();
		httpclient.getCredentialsProvider().setCredentials(new AuthScope(host, 80),
				new UsernamePasswordCredentials(username, password));

	}

	public static void main(String[] args) {
		LinksysReader reader = new LinksysReader("192.168.1.254", "/DHCPTable.asp", "admin", "admin");
		for (DhcpLease lease : reader.getDhcpLeases()) {
			System.out.printf("%s %s %s\n", lease.getClientHostName(), lease.getMacAddress(), lease.getExpiration());
		}
	}

	@Override
	public Set<DhcpLease> getDhcpLeases() {
		Set<DhcpLease> leases = parseUrl();
		return leases;
	}

	private Set<DhcpLease> parseUrl() {
		try {
			boolean nextLineContainsData = false;
			Set<DhcpLease> leases = new HashSet<DhcpLease>();
			String rawLeasesData = getRawLeasesData();
			for (String line : rawLeasesData.split("\n")) {
				if (line.contains("var table = new Array(")) {
					nextLineContainsData = true;
					continue;
				}
				if (nextLineContainsData) {
					if (line.contains(",")) {
						leases.add(parseLineAsDhcpLease(line));
					} else {
						break;
					}
				}
			}
			return leases;
		} catch (IOException e) {
			e.printStackTrace();
			return Collections.emptySet();
		}
	}

	protected String getRawLeasesData() throws IOException {
		HttpGet httpget = new HttpGet("http://" + host + pathPart);
		return httpclient.execute(httpget, new BasicResponseHandler());
	}

	private DhcpLease parseLineAsDhcpLease(String line) {
		// 'eeepc','192.168.1.150','00:23:54:7B:AF:97','15:26:32','150'
		// ,'android_fda9c953cc3242d8','192.168.1.151','90:21:55:B4:03:DD','12:45:00','151'
		line = line.trim();

		// remove first comma
		if (line.charAt(0) == ',') {
			line = line.substring(1);
		}

		String[] parts = stripFnutts(line.split(","));
		return new DhcpLeaseImpl(parts[0], parts[1], parts[2], parts[3]);
	}

	private String[] stripFnutts(String[] split) {
		for (int i = 0; i < split.length; i++) {
			split[i] = split[i].substring(1, split[i].length() - 1);
			if ("&nbsp;".equals(split[i])) {
				split[i] = "empty";
			}
		}
		return split;
	}

}
