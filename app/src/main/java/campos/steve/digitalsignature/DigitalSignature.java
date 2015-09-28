package campos.steve.digitalsignature;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.KeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;



public class DigitalSignature {

/*

    private final static String DOCINTRO = "docintro.properties";
    private Context context;

    public DigitalSignature(Context context) {
        this.context = context;
    }

    static InputStream getInputStream(String nameDocument)
            throws IOException
    {
        AssetManager am = context.getAssets();
        InputStream is = am.open(nameDocument);
        return is;
    }
    */

    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        // Generate a 256-bit key
        final int outputKeyLength = 256;

        SecureRandom secureRandom = new SecureRandom();
        // Do *not* seed secureRandom! Automatically seeded from system entropy.
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(outputKeyLength, secureRandom);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }

    public static Document add(Document doc, String parent, InputStream inputStream, InputStream keystoreFile) throws InstantiationException,
            IllegalAccessException, ClassNotFoundException, KeyStoreException,
            NoSuchAlgorithmException, CertificateException,
            FileNotFoundException, IOException, UnrecoverableEntryException,
            InvalidAlgorithmParameterException, KeyException, MarshalException,
            XMLSignatureException, XPathExpressionException {


        initPropertyFile(inputStream);
        /**  Load  KeyStore  **/
        /**  Load  KeyStore  **/
        Log.d("DIGITAL SIGNATURE P T",properties.getProperty("invoicec.signature.keystore.type"));
        Log.d("DIGITAL SIGNATURE P P",properties.getProperty("invoicec.signature.keystore.password"));
        Log.d("DIGITAL SIGNATURE D",KeyStore.getDefaultType());


        KeyStore keyStore = KeyStore.getInstance("BKS");
        keyStore.load(keystoreFile, properties.getProperty("invoicec.signature.keystore.password").toCharArray());
        Log.d("DIGITAL SIGNATURE D",""+keyStore.getKey("centelsa","steve".toCharArray()).toString());
        Log.d("DIGITAL SIGNATURE D",KeyStore.getDefaultType());
        //**  Load  Private Key  **//*
        //PrivateKey privateKey = (PrivateKey) keyStore.getKey(properties.getProperty("invoicec.signature.keystore.private_key_alias"), properties.getProperty("invoicec.signature.keystore.private_key_password").toCharArray());
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(properties.getProperty("invoicec.signature.keystore.private_key_alias"), "steve".toCharArray());
        //**  Load  Certificate  **//*
        X509Certificate cert = (X509Certificate) keyStore.getCertificate(properties.getProperty("invoicec.signature.keystore.private_key_alias"));
        Log.d("DIGITAL SIGNATURE C", cert.toString());

        /*KeyStore keyStore = KeyStore.getInstance("jks");
        X509Certificate cert = (X509Certificate) keyStore.getCertificate("centelsa");
        Key key = generateKey();
        Log.d("DIGITAL SIGNATURE K", key.toString()+"-"+key.getAlgorithm());*/

        /*Signature s = Signature.getInstance("SHA256withECDSA");
        s.initSign(((PrivateKeyEntry) entry).getPrivateKey());
        s.update(data);
        byte[] signature = s.sign();
        */

        /**  Load  Signed Info  **/
        String referenceURI = ""; // todo el documento
        String providerName = System.getProperty("jsr105Provider", "org.jcp.xml.dsig.internal.dom.XMLDSigRI");
        //String providerName =  "org.jcp.xml.dsig.internal.dom.XMLDSigRI";
        final XMLSignatureFactory sigFactory = XMLSignatureFactory.getInstance("DOM", (Provider) Class.forName(providerName).newInstance());
        Log.d("DIGITAL SIGNATURE s", sigFactory.toString()+"-"+sigFactory.getProvider());
        List<Transform> transforms = Collections.singletonList(sigFactory.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null));
        Log.d("DIGITAL SIGNATURE t", transforms.toString()+"-"+transforms.get(0));
        Reference ref = sigFactory.newReference(referenceURI, sigFactory.newDigestMethod(DigestMethod.SHA1, null), transforms, null, null);
        Log.d("DIGITAL SIGNATURE R", ref.toString()+"-"+ref.getCalculatedDigestValue());
        SignedInfo signedInfo = sigFactory.newSignedInfo(sigFactory.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE_WITH_COMMENTS,(C14NMethodParameterSpec)null),
                sigFactory.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(ref));
        Log.d("DIGITAL SIGNATURE S", signedInfo.toString()+"-"+signedInfo.getSignatureMethod());
        /**  Load  Key Info  **/
        KeyInfoFactory keyInfoFactory = sigFactory.getKeyInfoFactory();
        Log.d("DIGITAL SIGNATURE K", keyInfoFactory.toString()+"-"+keyInfoFactory.getMechanismType());
        List x509Content = new ArrayList();
        x509Content.add(cert.getSubjectX500Principal().getName());
        x509Content.add(cert);

        X509Data x509d = keyInfoFactory.newX509Data(x509Content);
        List<X509Data> keyInfoItems = new ArrayList<X509Data>();
        keyInfoItems.add(x509d);
        KeyInfo keyInfo = keyInfoFactory.newKeyInfo(keyInfoItems);

        /**  Calculate XPATH and NameSpace  **/
        XPath xpath = XPathFactory.newInstance().newXPath();
        Log.d("DIGITAL SIGNATUE X", ""+xpath);
        Node sigParent = (Node) xpath.compile("(//*/ExtensionContent)["+parent+"]").evaluate(doc, XPathConstants.NODE);
        Log.d("DIGITAL SIGNATUE s", ""+sigParent);
        DOMSignContext dsc = new DOMSignContext(privateKey, sigParent);
        Log.d("DIGITAL SIGNATUE s", ""+dsc.toString());
        dsc.setDefaultNamespacePrefix("ds");

        /**  Sign DOM Context **/
        XMLSignature signature = sigFactory.newXMLSignature(signedInfo, keyInfo, null, properties.getProperty("invoicec.signature.id"), null);
        signature.sign(dsc);
        return doc;
    }

    private static boolean initPropertyFile(InputStream inputStream) throws IOException{
        InputStream io = null;
        try
        {
            if(properties==null){
                properties = new Properties();
//                    io = new FileInputStream(System.getProperty("invoicec.context")+"docintro.properties");
                io = inputStream;
                Log.d("DIGITAL SIGNATURE IS",io.toString());
                properties.load(io);
                Log.d("DIGITAL SIGNATURE p", properties.toString());
            }
        }catch(IOException e){
            return false;
        }finally{
            try{
                if(io!=null){
                    io.close();
                }
            }catch(IOException e){
            }
        }
        return true;
    }

    static Properties properties;
}
