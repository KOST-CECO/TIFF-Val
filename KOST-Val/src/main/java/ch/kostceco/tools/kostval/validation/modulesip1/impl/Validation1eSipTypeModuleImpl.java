/* == KOST-Val ==================================================================================
 * The KOST-Val application is used for validate TIFF, SIARD, PDF/A, JP2, JPEG-Files and Submission
 * Information Package (SIP). Copyright (C) 2012-2017 Claire Roethlisberger (KOST-CECO), Christian
 * Eugster, Olivier Debenath, Peter Schneider (Staatsarchiv Aargau), Markus Hahn (coderslagoon),
 * Daniel Ludin (BEDAG AG)
 * -----------------------------------------------------------------------------------------------
 * KOST-Val is a development of the KOST-CECO. All rights rest with the KOST-CECO. This application
 * is free software: you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version. BEDAG AG and Daniel Ludin hereby disclaims all copyright
 * interest in the program SIP-Val v0.2.0 written by Daniel Ludin (BEDAG AG). Switzerland, 1 March
 * 2011. This application is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the follow GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA or see
 * <http://www.gnu.org/licenses/>.
 * ============================================================================================== */

package ch.kostceco.tools.kostval.validation.modulesip1.impl;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.kostceco.tools.kostval.exception.modulesip1.Validation1eSipTypeException;
import ch.kostceco.tools.kostval.service.ConfigurationService;
import ch.kostceco.tools.kostval.validation.ValidationModuleImpl;
import ch.kostceco.tools.kostval.validation.modulesip1.Validation1eSipTypeModule;

/** Der SIP Typ wird ermittelt: GEVER oder FILE (ermittelt aus dem metadata.xml, element ablieferung) */
public class Validation1eSipTypeModuleImpl extends ValidationModuleImpl implements
		Validation1eSipTypeModule
{

	private ConfigurationService	configurationService;

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	public void setConfigurationService( ConfigurationService configurationService )
	{
		this.configurationService = configurationService;
	}

	@Override
	public boolean validate( File valDatei, File directoryOfLogfile )
			throws Validation1eSipTypeException
	{
		// Informationen zur Darstellung "onWork" holen
		String onWork = getConfigurationService().getShowProgressOnWork();
		/* Nicht vergessen in "src/main/resources/config/applicationContext-services.xml" beim
		 * entsprechenden Modul die property anzugeben: <property name="configurationService"
		 * ref="configurationService" /> */
		if ( onWork.equals( "no" ) ) {
			// keine Ausgabe
		} else {
			// Ausgabe SIP-Modul Ersichtlich das KOST-Val arbeitet
			System.out.print( "1E   " );
			System.out.print( "\b\b\b\b\b" );
		}

		try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			// dbf.setValidating(false);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse( new FileInputStream( new File( valDatei.getAbsolutePath()
					+ File.separator + "header" + File.separator + "metadata.xml" ) ) );
			doc.getDocumentElement().normalize();

			dbf.setFeature( "http://xml.org/sax/features/namespaces", false );

			/* Aktuelle Lösung funktioniert nur wenn kein Präfix beim Elementnamen erlaubt ist!
			 * 
			 * IO: ablieferung
			 * 
			 * NIO: v4:ablieferung 
			 * 
			 * Wird neu in 1d kontrolliert*/

			NodeList layerConfigList = doc.getElementsByTagName( "ablieferung" );

			Node node = layerConfigList.item( 0 );
			Element e = (Element) node;
			String name = e.getAttribute( "xsi:type" );

			if ( name.contains( "ablieferungGeverSIP" ) ) {
				// GEVER-SIP
			} else if ( name.contains( "ablieferungFilesSIP" ) ) {
				// FILE-SIP
			} else {
				getMessageService().logError(
						getTextResourceService().getText( MESSAGE_XML_MODUL_Ae_SIP )
								+ getTextResourceService().getText( ERROR_XML_AE_ABLIEFERUNGSTYPUNDEFINED ) );
				return false;
			}

		} catch ( Exception e ) {
			getMessageService().logError(
					getTextResourceService().getText( MESSAGE_XML_MODUL_Ae_SIP )
							+ getTextResourceService().getText( ERROR_XML_UNKNOWN, e.getMessage() ) );
			return false;
		}

		return true;
	}

}
