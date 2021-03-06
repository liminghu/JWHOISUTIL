package com.jwhois.util;

import java.io.File;
import java.io.IOException;
import java.net.IDN;
import java.util.Arrays;

import com.jwhois.core.Utility;
import com.maxmind.geoip.Country;
import com.maxmind.geoip.LookupService;

public class GeoIPUtil {

	private LookupService	geoService;

	public GeoIPUtil(File dataFile) {
		try {
			geoService = new LookupService( dataFile, LookupService.GEOIP_MEMORY_CACHE );
		}
		catch (IOException e) {
			Utility.logWarn( "GeoIPUtil::getGeoIPInfo IOException: <filepath:" + dataFile.getPath() + ">", e );
		}
	}

	public String[] getGeoIPInfo(String dom) {
		String[] res = new String[3];
		Arrays.fill( res, "" );

		if (geoService == null)
			return res;

		dom = IDN.toASCII( dom );
		if (!Utility.isValidDom( dom ))
			return res;

		if (geoService != null) {
			String ip = Utility.getAddressbyName( dom );
			if (Utility.isValidIP( ip )) {
				res[0] = ip;
				Country country = geoService.getCountry( ip );
				if (null != country) {
					res[1] = country.getName();
					res[2] = country.getCode();
				}
			}
		}
		return res;
	}

	public void close() {
		if (geoService != null)
			geoService.close();
	}
}
