/* == KOST-Val ==================================================================================
 * The KOST-Val application is used for validate TIFF, SIARD, PDF/A, JP2, JPEG-Files and Submission
 * Information Package (SIP). Copyright (C) 2012-2015 Claire R�thlisberger (KOST-CECO), Christian
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
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import ch.kostceco.tools.kostval.exception.modulesip1.Validation1dMetadataException;
import ch.kostceco.tools.kostval.service.ConfigurationService;
import ch.kostceco.tools.kostval.validation.ValidationModuleImpl;
import ch.kostceco.tools.kostval.validation.modulesip1.Validation1dMetadataModule;

public class Validation1dMetadataModuleImpl extends ValidationModuleImpl implements
		Validation1dMetadataModule
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

	final int	BUFFER	= 2048;

	public boolean validate( File valDatei, File directoryOfLogfile )
			throws Validation1dMetadataException
	{
		// Ausgabe SIP-Modul Ersichtlich das KOST-Val arbeitet
		System.out.print( "1D   " );
		System.out.print( "\r" );

		boolean result = false;
		String sipVer = "ECH1.0.txt";
		File sipVersionFile;

		try {
			File xmlToValidate = new File( valDatei.getAbsolutePath() + File.separator + "header"
					+ File.separator + "metadata.xml" );
			File xsdToValidateBar1 = new File( valDatei.getAbsolutePath() + File.separator + "header"
					+ File.separator + "xsd" + File.separator + "arelda_v3.13.2.xsd" );
			File xsdToValidateEch1 = new File( valDatei.getAbsolutePath() + File.separator + "header"
					+ File.separator + "xsd" + File.separator + "arelda.xsd" );

			String allowedV4 = getConfigurationService().getAllowedVersionBar4Ech1();
			String allowedV1 = getConfigurationService().getAllowedVersionBar1();

			if ( (xmlToValidate.exists() && xsdToValidateBar1.exists()) || allowedV4.startsWith( "0" ) ) {
				// Schemavalidierung Nach Version BAR 1
				sipVer = "BAR1.txt";
				sipVersionFile = new File( directoryOfLogfile.getAbsolutePath() + File.separator + sipVer );
				try {
					sipVersionFile.createNewFile();
				} catch ( IOException e ) {
					e.printStackTrace();
				}

				try {
					System.setProperty( "javax.xml.parsers.DocumentBuilderFactory",
							"org.apache.xerces.jaxp.DocumentBuilderFactoryImpl" );
					DocumentBuilderFactory factoryMsg = DocumentBuilderFactory.newInstance();
					factoryMsg.setNamespaceAware( true );
					factoryMsg.setValidating( true );
					factoryMsg.setAttribute( "http://java.sun.com/xml/jaxp/properties/schemaLanguage",
							"http://www.w3.org/2001/XMLSchema" );
					factoryMsg.setAttribute( "http://java.sun.com/xml/jaxp/properties/schemaSource",
							xsdToValidateBar1.getAbsolutePath() );
					DocumentBuilder builderMsg = factoryMsg.newDocumentBuilder();
					ValidatorMsg handlerMsg = new ValidatorMsg();
					builderMsg.setErrorHandler( handlerMsg );
					builderMsg.parse( xmlToValidate.getAbsolutePath() );
					if ( handlerMsg.validationErrorMsg == true ) {
						result = false;
					} else {
						result = true;
					}
				} catch ( java.io.IOException ioe ) {
					getMessageService().logError(
							getTextResourceService().getText( MESSAGE_XML_MODUL_Ad_SIP )
									+ getTextResourceService().getText( ERROR_XML_UNKNOWN,
											ioe.getMessage() + " (IOException)" ) );
					result = false;
				} catch ( SAXException e ) {
					getMessageService().logError(
							getTextResourceService().getText( MESSAGE_XML_MODUL_Ad_SIP )
									+ getTextResourceService().getText( ERROR_XML_UNKNOWN,
											e.getMessage() + " (SAXException)" ) );
					result = false;
				} catch ( ParserConfigurationException e ) {
					getMessageService().logError(
							getTextResourceService().getText( MESSAGE_XML_MODUL_Ad_SIP )

									+ getTextResourceService().getText( ERROR_XML_UNKNOWN,
											e.getMessage() + " (ParserConfigurationException)" ) );
					result = false;
				}
			} else if ( (xmlToValidate.exists() && xsdToValidateEch1.exists())
					|| allowedV1.startsWith( "0" ) ) {
				// Schmavalidierung nach eCH Version 1 inkl Addendum in den Ressourcen. Dies erfolgt mit
				// einer Validierung �bers Kreuz
				try {
					if ( allowedV4.startsWith( "1" ) ) {
						/* Validierung nach eCH-0160v1.0. Dies ist auf jeden Fall erlaubt, auch wenn 1.1
						 * definiert wurde */
						// xmlToValidate mit xsdToValidateEch1Add
						File xsdToValidateEch1Add = new File( "resources" + File.separator + "header_1d"
								+ File.separator + "eCH-0160v1.0" + File.separator + "xsd" + File.separator
								+ "arelda.xsd" );
						System.setProperty( "javax.xml.parsers.DocumentBuilderFactory",
								"org.apache.xerces.jaxp.DocumentBuilderFactoryImpl" );
						DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
						factory.setNamespaceAware( true );
						factory.setValidating( true );
						factory.setAttribute( "http://java.sun.com/xml/jaxp/properties/schemaLanguage",
								"http://www.w3.org/2001/XMLSchema" );
						factory.setAttribute( "http://java.sun.com/xml/jaxp/properties/schemaSource",
								xsdToValidateEch1Add.getAbsolutePath() );
						DocumentBuilder builder = factory.newDocumentBuilder();
						Validator handler = new Validator();
						builder.setErrorHandler( handler );
						builder.parse( xmlToValidate.getAbsolutePath() );
						if ( handler.validationError == true ) {
							/* Validierungsfehler. Wenn eCH-0160v1.1 erlaubt ist, wird noch danach validiert
							 * ansonsten ist es invalide. */
							result = false;
							// prov auf invalide setzten
							if ( allowedV4.startsWith( "1.1" ) ) {
								/* Validierung nach eCH-0160v1.1. da 1.0 invalide */
								sipVer = "ECH1.1.txt";
								sipVersionFile = new File( directoryOfLogfile.getAbsolutePath() + File.separator
										+ sipVer );
								try {
									sipVersionFile.createNewFile();
								} catch ( IOException e ) {
									e.printStackTrace();
								}

								// xmlToValidate mit xsdToValidateEch1Add11
								File xsdToValidateEch1Add11 = new File( "resources" + File.separator + "header_1d"
										+ File.separator + "eCH-0160v1.1" + File.separator + "xsd" + File.separator
										+ "arelda.xsd" );
								System.setProperty( "javax.xml.parsers.DocumentBuilderFactory",
										"org.apache.xerces.jaxp.DocumentBuilderFactoryImpl" );
								DocumentBuilderFactory factoryMsg = DocumentBuilderFactory.newInstance();
								factoryMsg.setNamespaceAware( true );
								factoryMsg.setValidating( true );
								factoryMsg.setAttribute( "http://java.sun.com/xml/jaxp/properties/schemaLanguage",
										"http://www.w3.org/2001/XMLSchema" );
								factoryMsg.setAttribute( "http://java.sun.com/xml/jaxp/properties/schemaSource",
										xsdToValidateEch1Add11.getAbsolutePath() );
								DocumentBuilder builderMsg = factoryMsg.newDocumentBuilder();
								ValidatorMsg handlerMsg = new ValidatorMsg();
								builderMsg.setErrorHandler( handlerMsg );
								builderMsg.parse( xmlToValidate.getAbsolutePath() );
								if ( handlerMsg.validationErrorMsg == true ) {
									/* Validierungsfehler auch mit eCH-0160v1.1 --> invalide. */
									result = false;
								} else {
									/* Validierung mit eCH-0160v1.1 bestanden --> valide. */
									result = true;
								}
							} else {
								/* Validierung nach eCH-0160v1.0 bleibt invalide da 1.1 nicht erlaubt */
								// revalidierung jetzt aber mit Fehlerausgabe
								// xmlToValidate mit xsdToValidateEch1Add
								File xsdToValidateEch1AddMsg = new File( "resources" + File.separator + "header_1d"
										+ File.separator + "eCH-0160v1.0" + File.separator + "xsd" + File.separator
										+ "arelda.xsd" );
								System.setProperty( "javax.xml.parsers.DocumentBuilderFactory",
										"org.apache.xerces.jaxp.DocumentBuilderFactoryImpl" );
								DocumentBuilderFactory factoryMsg = DocumentBuilderFactory.newInstance();
								factoryMsg.setNamespaceAware( true );
								factoryMsg.setValidating( true );
								factoryMsg.setAttribute( "http://java.sun.com/xml/jaxp/properties/schemaLanguage",
										"http://www.w3.org/2001/XMLSchema" );
								factoryMsg.setAttribute( "http://java.sun.com/xml/jaxp/properties/schemaSource",
										xsdToValidateEch1AddMsg.getAbsolutePath() );
								DocumentBuilder builderMsg = factoryMsg.newDocumentBuilder();
								ValidatorMsg handlerMsg = new ValidatorMsg();
								builderMsg.setErrorHandler( handlerMsg );
								builderMsg.parse( xmlToValidate.getAbsolutePath() );
								if ( handlerMsg.validationErrorMsg == true ) {
									/* Validierungsfehler auch mit eCH-0160v1.1 --> invalide. */
									result = false;
								}
								sipVer = "ECH1.0.txt";
								sipVersionFile = new File( directoryOfLogfile.getAbsolutePath() + File.separator
										+ sipVer );
								try {
									sipVersionFile.createNewFile();
								} catch ( IOException e ) {
									e.printStackTrace();
								}
								result = false;
							}
						} else {
							/* Validierung nach eCH-0160v1.0 da 1.0 valide */
							result = true;
							sipVer = "ECH1.0.txt";
							sipVersionFile = new File( directoryOfLogfile.getAbsolutePath() + File.separator
									+ sipVer );
							try {
								sipVersionFile.createNewFile();
							} catch ( IOException e ) {
								e.printStackTrace();
							}
						}
					}
				} catch ( java.io.IOException ioe ) {
					getMessageService().logError(
							getTextResourceService().getText( MESSAGE_XML_MODUL_Ad_SIP )
									+ getTextResourceService().getText( ERROR_XML_UNKNOWN,
											ioe.getMessage() + " (IOException)" ) );
					result = false;
				} catch ( SAXException e ) {
					getMessageService().logError(
							getTextResourceService().getText( MESSAGE_XML_MODUL_Ad_SIP )
									+ getTextResourceService().getText( ERROR_XML_UNKNOWN,
											e.getMessage() + " (SAXException)" ) );
					result = false;
				} catch ( ParserConfigurationException e ) {
					getMessageService().logError(
							getTextResourceService().getText( MESSAGE_XML_MODUL_Ad_SIP )

									+ getTextResourceService().getText( ERROR_XML_UNKNOWN,
											e.getMessage() + " (ParserConfigurationException)" ) );
					result = false;
				}
			}
		} catch ( Exception e ) {
			getMessageService().logError(
					getTextResourceService().getText( MESSAGE_XML_MODUL_Ad_SIP )
							+ getTextResourceService().getText( ERROR_XML_UNKNOWN, e.getMessage() ) );
			return false;
		}
		return result;
	}

	private class Validator extends DefaultHandler
	{
		// Validierung OHNE Fehlermeldung
		public boolean	validationError	= false;

		public void error( SAXParseException exception ) throws SAXException
		{
			validationError = true;
			// ohne Msg, da ggf Validierung nach eCH 1.1 valide ist und Fehler nach 1.0 nicht ausgegeben
			// werden d�rfen
		}

		public void fatalError( SAXParseException exception ) throws SAXException
		{
			validationError = true;
			// ohne Msg, da ggf Validierung nach eCH 1.1 valide ist und Fehler nach 1.0 nicht ausgegeben
			// werden d�rfen
		}

		public void warning( SAXParseException exception ) throws SAXException
		{
		}
	}

	private class ValidatorMsg extends DefaultHandler
	{
		// Validierung mit Fehlermeldung
		public boolean						validationErrorMsg		= false;

		public SAXParseException	saxParseExceptionMsg	= null;

		public void error( SAXParseException exception ) throws SAXException
		{
			validationErrorMsg = true;
			saxParseExceptionMsg = exception;
			getMessageService().logError(
					getTextResourceService().getText( MESSAGE_XML_MODUL_Ad_SIP )
							+ getTextResourceService().getText( ERROR_XML_AD_METADATA_ERRORS,
									saxParseExceptionMsg.getLineNumber(), saxParseExceptionMsg.getMessage() ) );

		}

		public void fatalError( SAXParseException exception ) throws SAXException
		{
			validationErrorMsg = true;
			saxParseExceptionMsg = exception;
			getMessageService().logError(
					getTextResourceService().getText( MESSAGE_XML_MODUL_Ad_SIP )
							+ getTextResourceService().getText( ERROR_XML_AD_METADATA_ERRORS,
									saxParseExceptionMsg.getLineNumber(), saxParseExceptionMsg.getMessage() ) );
		}

		public void warning( SAXParseException exception ) throws SAXException
		{
		}
	}

}
