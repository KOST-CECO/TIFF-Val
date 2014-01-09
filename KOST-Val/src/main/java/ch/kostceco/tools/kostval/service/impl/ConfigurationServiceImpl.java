/*== KOST-Val ==================================================================================
The KOST-Val application is used for validate TIFF, SIARD, and PDF/A-Files. 
Copyright (C) 2012-2013 Claire R�thlisberger (KOST-CECO), Christian Eugster, Olivier Debenath, 
Peter Schneider (Staatsarchiv Aargau)
-----------------------------------------------------------------------------------------------
KOST-Val is a development of the KOST-CECO. All rights rest with the KOST-CECO. 
This application is free software: you can redistribute it and/or modify it under the 
terms of the GNU General Public License as published by the Free Software Foundation, 
either version 3 of the License, or (at your option) any later version. 
This application is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
See the follow GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program; 
if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, 
Boston, MA 02110-1301 USA or see <http://www.gnu.org/licenses/>.
==============================================================================================*/

package ch.kostceco.tools.kostval.service.impl;

import java.io.File;
import java.net.URL;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import ch.kostceco.tools.kostval.KOSTVal;
import ch.kostceco.tools.kostval.logging.Logger;
import ch.kostceco.tools.kostval.service.ConfigurationService;
import ch.kostceco.tools.kostval.service.TextResourceService;

public class ConfigurationServiceImpl implements ConfigurationService
{

	private static final Logger	LOGGER	= new Logger(
												ConfigurationServiceImpl.class );
	XMLConfiguration			config	= null;
	private TextResourceService	textResourceService;

	public TextResourceService getTextResourceService()
	{
		return textResourceService;
	}

	public void setTextResourceService( TextResourceService textResourceService )
	{
		this.textResourceService = textResourceService;
	}

	private XMLConfiguration getConfig()
	{
		if ( this.config == null ) {

			try {

				String path = "configuration/kostval.conf.xml";

				URL locationOfJar = KOSTVal.class.getProtectionDomain()
						.getCodeSource().getLocation();
				String locationOfJarPath = locationOfJar.getPath();

				if ( locationOfJarPath.endsWith( ".jar" ) ) {
					File file = new File( locationOfJarPath );
					String fileParent = file.getParent();
					path = fileParent + "/" + path;
				}

				config = new XMLConfiguration( path );

			} catch ( ConfigurationException e ) {
				LOGGER.logInfo( getTextResourceService().getText(
						MESSAGE_CONFIGURATION_ERROR_1 ) );
				LOGGER.logInfo( getTextResourceService().getText(
						MESSAGE_CONFIGURATION_ERROR_2 ) );
				LOGGER.logInfo( getTextResourceService().getText(
						MESSAGE_CONFIGURATION_ERROR_3 ) );
				System.exit( 1 );
			}
		}
		return config;
	}

	@Override
	public String getPathToWorkDir()
	{
		/**
		 * Gibt den Pfad des Arbeitsverzeichnisses zur�ck. Dieses Verzeichnis
		 * wird zum Entpacken des .zip-Files verwendet.
		 * 
		 * @return Pfad des Arbeitsverzeichnisses
		 */
		Object prop = getConfig().getProperty( "pathtoworkdir" );
		if ( prop instanceof String ) {
			String value = (String) prop;
			return value;
		}
		return null;
	}

	@Override
	public String getPathToJhoveConfiguration()
	{
		/**
		 * Gibt den Pfad des jhove.conf zur�ck.
		 */
		Object prop = getConfig().getProperty( "pathtojhoveconfig" );
		if ( prop instanceof String ) {
			String value = (String) prop;
			return value;
		}
		return null;
	}

	/*--- PDF/A ---------------------------------------------------------------------*/
	@Override
	public String getPathToPdftronExe()
	{
		Object prop = getConfig().getProperty( "pdfa.pathtopdftronexe" );

		if ( prop instanceof String ) {
			String value = (String) prop;
			return value;
		}
		return null;
	}

	@Override
	public String pdfa1()
	{
		Object prop = getConfig().getProperty( "pdfa.pdfa1" );

		if ( prop instanceof String ) {
			String value = (String) prop;
			return value;
		}
		return null;
	}

	@Override
	public String pdfa2()
	{
		Object prop = getConfig().getProperty( "pdfa.pdfa2" );

		if ( prop instanceof String ) {
			String value = (String) prop;
			return value;
		}
		return null;
	}

	/*--- SIARD ---------------------------------------------------------------------*/
	@Override
	public int getTableRowsLimit()
	{
		/**
		 * Gibt die maximale Anzahl von Rows zur�ck. Dieser Wert wird in Modul H
		 * verwendet. Module H validiert die table.xml Dateien gegen ihre
		 * table.xsd Schemas. Wenn ein Schema <xs:element name="row"
		 * type="rowType" minOccurs="0" maxOccurs="unbounded"/> in minOccurs
		 * oder maxOccurs hohe Zahlenwerte enth�lt, f�hrt die Validierung zu
		 * einem java.lang.OutOfMemoryError. Da dieser Error nicht aufgefangen
		 * werden kann, werden vor der Validierung die Rows der Tabelle gez�hlt.
		 * Die ermittelte Zahl darf nicht �ber dem hier zur�ckgegebenen Wert
		 * liegen.
		 */
		int value = 20000;
		Object prop = getConfig().getProperty( "siard.table-rows-limit" );
		if ( prop != null ) {
			try {
				value = Integer.valueOf( prop.toString() ).intValue();
			} catch ( NumberFormatException e ) {
				// Do nothing
			}
		}
		return value;
	}

	/*--- TIFF ---------------------------------------------------------------------*/
	// AllowedCompression
	@Override
	public String getAllowedCompression1()
	{
		Object prop = getConfig().getProperty(
				"tiff.allowedcompression.allowedcompression1" );
		if ( prop instanceof String ) {
			String value = (String) prop;
			return value;
		}
		return null;
	}

	@Override
	public String getAllowedCompression2()
	{
		Object prop = getConfig().getProperty(
				"tiff.allowedcompression.allowedcompression2" );
		if ( prop instanceof String ) {
			String value = (String) prop;
			return value;
		}
		return null;
	}

	@Override
	public String getAllowedCompression3()
	{
		Object prop = getConfig().getProperty(
				"tiff.allowedcompression.allowedcompression3" );
		if ( prop instanceof String ) {
			String value = (String) prop;
			return value;
		}
		return null;
	}

	@Override
	public String getAllowedCompression4()
	{
		Object prop = getConfig().getProperty(
				"tiff.allowedcompression.allowedcompression4" );
		if ( prop instanceof String ) {
			String value = (String) prop;
			return value;
		}
		return null;
	}

	@Override
	public String getAllowedCompression5()
	{
		Object prop = getConfig().getProperty(
				"tiff.allowedcompression.allowedcompression5" );
		if ( prop instanceof String ) {
			String value = (String) prop;
			return value;
		}
		return null;
	}

	@Override
	public String getAllowedCompression7()
	{
		Object prop = getConfig().getProperty(
				"tiff.allowedcompression.allowedcompression7" );
		if ( prop instanceof String ) {
			String value = (String) prop;
			return value;
		}
		return null;
	}

	@Override
	public String getAllowedCompression8()
	{
		Object prop = getConfig().getProperty(
				"tiff.allowedcompression.allowedcompression8" );
		if ( prop instanceof String ) {
			String value = (String) prop;
			return value;
		}
		return null;
	}

	@Override
	public String getAllowedCompression32773()
	{
		Object prop = getConfig().getProperty(
				"tiff.allowedcompression.allowedcompression32773" );
		if ( prop instanceof String ) {
			String value = (String) prop;
			return value;
		}
		return null;
	}

	// AllowedPhotointer
	@Override
	public String getAllowedPhotointer0()
	{
		Object prop = getConfig().getProperty(
				"tiff.allowedphotointer.allowedphotointer0" );
		if ( prop instanceof String ) {
			String value = (String) prop;
			return value;
		}
		return null;
	}

	@Override
	public String getAllowedPhotointer1()
	{
		Object prop = getConfig().getProperty(
				"tiff.allowedphotointer.allowedphotointer1" );
		if ( prop instanceof String ) {
			String value = (String) prop;
			return value;
		}
		return null;
	}

	@Override
	public String getAllowedPhotointer2()
	{
		Object prop = getConfig().getProperty(
				"tiff.allowedphotointer.allowedphotointer2" );
		if ( prop instanceof String ) {
			String value = (String) prop;
			return value;
		}
		return null;
	}

	@Override
	public String getAllowedPhotointer3()
	{
		Object prop = getConfig().getProperty(
				"tiff.allowedphotointer.allowedphotointer3" );
		if ( prop instanceof String ) {
			String value = (String) prop;
			return value;
		}
		return null;
	}

	@Override
	public String getAllowedPhotointer4()
	{
		Object prop = getConfig().getProperty(
				"tiff.allowedphotointer.allowedphotointer4" );
		if ( prop instanceof String ) {
			String value = (String) prop;
			return value;
		}
		return null;
	}

	@Override
	public String getAllowedPhotointer5()
	{
		Object prop = getConfig().getProperty(
				"tiff.allowedphotointer.allowedphotointer5" );
		if ( prop instanceof String ) {
			String value = (String) prop;
			return value;
		}
		return null;
	}

	@Override
	public String getAllowedPhotointer6()
	{
		Object prop = getConfig().getProperty(
				"tiff.allowedphotointer.allowedphotointer6" );
		if ( prop instanceof String ) {
			String value = (String) prop;
			return value;
		}
		return null;
	}

	@Override
	public String getAllowedPhotointer8()
	{
		Object prop = getConfig().getProperty(
				"tiff.allowedphotointer.allowedphotointer8" );
		if ( prop instanceof String ) {
			String value = (String) prop;
			return value;
		}
		return null;
	}

	// AllowedBitspersample
	@Override
	public String getAllowedBitspersample1()
	{
		Object prop = getConfig().getProperty(
				"tiff.allowedbitspersample.allowedbitspersample1" );
		if ( prop instanceof String ) {
			String value = (String) prop;
			return value;
		}
		return null;
	}

	@Override
	public String getAllowedBitspersample2()
	{
		Object prop = getConfig().getProperty(
				"tiff.allowedbitspersample.allowedbitspersample2" );
		if ( prop instanceof String ) {
			String value = (String) prop;
			return value;
		}
		return null;
	}

	@Override
	public String getAllowedBitspersample4()
	{
		Object prop = getConfig().getProperty(
				"tiff.allowedbitspersample.allowedbitspersample4" );
		if ( prop instanceof String ) {
			String value = (String) prop;
			return value;
		}
		return null;
	}

	@Override
	public String getAllowedBitspersample8()
	{
		Object prop = getConfig().getProperty(
				"tiff.allowedbitspersample.allowedbitspersample8" );
		if ( prop instanceof String ) {
			String value = (String) prop;
			return value;
		}
		return null;
	}

	@Override
	public String getAllowedBitspersample16()
	{
		Object prop = getConfig().getProperty(
				"tiff.allowedbitspersample.allowedbitspersample16" );
		if ( prop instanceof String ) {
			String value = (String) prop;
			return value;
		}
		return null;
	}

	@Override
	public String getAllowedBitspersample32()
	{
		Object prop = getConfig().getProperty(
				"tiff.allowedbitspersample.allowedbitspersample32" );
		if ( prop instanceof String ) {
			String value = (String) prop;
			return value;
		}
		return null;
	}

	@Override
	public String getAllowedBitspersample64()
	{
		Object prop = getConfig().getProperty(
				"tiff.allowedbitspersample.allowedbitspersample64" );
		if ( prop instanceof String ) {
			String value = (String) prop;
			return value;
		}
		return null;
	}

	// AllowedMultipage
	@Override
	public String getAllowedMultipage()
	{
		Object prop = getConfig().getProperty(
				"tiff.allowedother.allowedmultipage" );
		if ( prop instanceof String ) {
			String value = (String) prop;
			return value;
		}
		return null;
	}

	// AllowedTiles
	@Override
	public String getAllowedTiles()
	{
		Object prop = getConfig()
				.getProperty( "tiff.allowedother.allowedtiles" );
		if ( prop instanceof String ) {
			String value = (String) prop;
			return value;
		}
		return null;
	}

	// AllowedSize
	@Override
	public String getAllowedSize()
	{
		Object prop = getConfig().getProperty( "tiff.allowedother.allowedsize" );
		if ( prop instanceof String ) {
			String value = (String) prop;
			return value;
		}
		return null;
	}
}
