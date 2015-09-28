package campos.steve.digitalsignature;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;


public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private EditText editText;
    private Button button;
    private Context contexto;
    String textoSHA1 = null;
    //FileInputStream fis = null;
    private final static String DOCINTRO = "docintro.properties";
    private final static String DOCUMENTO  ="20138122256-01-F101-00000007.XML";
    private final static String BKS  ="union.bks";

    private Document documentoFirmado;
    private Document docSinFirmar;
    private InputStream keystore;

    Document getDocumentFromFilePath(String filePath)
            throws IOException, ParserConfigurationException, SAXException {
        AssetManager am = contexto.getAssets();
        InputStream is = am.open(filePath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(is);
    }

    InputStream getFilefromAssets(String nameDocument)
            throws IOException
    {
        AssetManager am = contexto.getAssets();
        return am.open(nameDocument);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contexto = getApplicationContext();



        try {
            docSinFirmar = getDocumentFromFilePath(DOCUMENTO);
            keystore = getFilefromAssets(BKS);
            //documentoFirmado = DigitalSignature.add(doc, "UBLExtensions",getFilefromAssets(DOCINTRO));


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }





        /*final HandlerXML handlerXML = new HandlerXML();
        handlerXML.escribirXML(contexto);*/

        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String texto = editText.getText().toString();



                try {
                    documentoFirmado = DigitalSignature.add(docSinFirmar, texto,getFilefromAssets(DOCINTRO), keystore);

                    textView.setText(" FUCK OFF, NO ME SALE XD.\n"+documentoFirmado.getTextContent());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (UnrecoverableKeyException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (KeyException e) {
                    e.printStackTrace();
                } catch (MarshalException e) {
                    e.printStackTrace();
                } catch (XMLSignatureException e) {
                    e.printStackTrace();
                } catch (XPathExpressionException e) {
                    e.printStackTrace();
                } catch (UnrecoverableEntryException e) {
                    e.printStackTrace();
                }


                //new GenerateDigitalSignature().execute();


                //String texto = null;

                /*byte[] byteReads = null;
                try {
                    byteReads = handlerXML.leerXML(contexto);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
                try {

                    //String textoSHA1 = CodigoSHA1.SHA1(texto);
                    textoSHA1 = CodigoSHA1.SHA1(byteReads);
                    String str = new String(byteReads, "iso-8859-1");
                    //textView.append(str + " a SHA-1: " + textoSHA1 + ".\n");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                try {
                    byte[] SHA1Generate = handlerXML.putSHA1toXML(getApplicationContext(), textoSHA1);
                    String str = new String(SHA1Generate, "iso-8859-1");
                    textView.append(str + " FIRMADO.\n");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (TransformerException e) {
                    e.printStackTrace();
                }*/

            }
        });
    }

    class GenerateDigitalSignature extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {

            final HandlerXML handlerXML = new HandlerXML();
            handlerXML.escribirXML(contexto);

            byte[] byteReads = null;
            try {
                byteReads = handlerXML.leerXML(contexto);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            try {

                //String textoSHA1 = CodigoSHA1.SHA1(texto);
                textoSHA1 = CodigoSHA1.SHA1(byteReads);
                String str = new String(byteReads, "iso-8859-1");
                //textView.append(str + " a SHA-1: " + textoSHA1 + ".\n");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            try {
                byte[] SHA1Generate = handlerXML.putSHA1toXML(getApplicationContext(), textoSHA1);
                String str = new String(SHA1Generate, "iso-8859-1");
                textView.append(str + " FIRMADO.\n");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            }
            return "LOL";
        }
    }

/*

    public byte[] leerXML(Context context) throws IOException, ParserConfigurationException, SAXException {
        fis = context.openFileInput("test.xml");
        */
/*InputStream is = context.openFileInput("test.xml");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        *//*

        //System.out.println(file.exists() + "!!");
        //InputStream in = resource.openStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[(int) fis.getChannel().size()];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                bos.write(buf, 0, readNum); //no doubt here is 0
                //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
                //System.out.println("read " + readNum + " bytes,");
            }
        } catch (IOException ex) {
            //Logger.getLogger(genJpeg.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] bytes = bos.toByteArray();
        return bytes;
*/
/*
        //DOM (Por ejemplo)
        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();

        Document dom = builder.parse(fil);

        StreamResult streamResult = new StreamResult(fil);


        String documentoTexto = dom.toString();

        //A partir de aquí se trataría el árbol DOM como siempre.
        //Por ejemplo:
        Element root = dom.getDocumentElement();

        return documentoTexto;*//*

    }

    public byte[] putSHA1toXML(Context context, String SHA1) throws IOException, ParserConfigurationException, SAXException, TransformerException {


        FileInputStream file = context.openFileInput("test.xml");
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(file);

        // Change the content of node
        Node nodes = doc.getElementsByTagName("ds:DigestValue").item(0);
        Log.d("NODES DIGESTVALUE", "" + nodes.getNodeName() + "," + nodes.getTextContent() + "," + nodes.getNodeValue() + "," + nodes.getLocalName());

        nodes.setNodeValue(SHA1);
        nodes.setTextContent(SHA1);
        Log.d("NODES SHA1 PUT",""+nodes.getTextContent());

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

*/
/*
        File roott = Environment.getExternalStorageDirectory();
        File filee = new File(roott, "factura/test.xml");
*//*


        FileOutputStream fileOutput= context.openFileOutput("test.xml", MODE_PRIVATE);

        // initialize StreamResult with File object to save to file
        //StreamResult resultSinFirmar = new StreamResult(new FileOutputStream(getFileStreamPath("test.xml")));

        */
/*File myFile = new File("/sdcard/test.xml");
        boolean created = myFile.createNewFile();
        Log.d("FILE CREATED", ""+created);
        FileOutputStream fOut = new FileOutputStream(myFile);*//*


        Log.d("FILE DIR",""+context.getFilesDir());
        File myFile = new File(context.getFilesDir(), "test3.xml");
        StreamResult result = new StreamResult(myFile);
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);





        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[(int) fis.getChannel().size()];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                bos.write(buf, 0, readNum); //no doubt here is 0
                //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
                //System.out.println("read " + readNum + " bytes,");
            }
        } catch (IOException ex) {
            //Logger.getLogger(genJpeg.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] bytes = bos.toByteArray();


        */
/*Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        // initialize StreamResult with File object to save to file
        StreamResult result = new StreamResult(filee);
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);*//*




        NodeList nodeList= doc.getChildNodes();

        for (int i= 0; i<nodeList.getLength(); i++){

            Node node = nodeList.item(i);
            NodeList nodeListSon = node.getChildNodes();

            for (int j= 0; j<nodeListSon.getLength(); j++){
                Log.d("NODES", "" + nodeListSon.item(j).getNodeName());
            }

        }

        return bytes;
       */
/* try {
            File root = Environment.getExternalStorageDirectory();
            File file1 = new File(root, "factura/test2.xml");

            FileInputStream file= context.openFileInput("test.xml");
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(file);
            // Change the content of node
            Node nodes = doc.getElementsByTagName("DigestValue").item(0);

            NodeList keyList = doc.getElementsByTagName("DigestValue");
            Node Keynode = keyList.item(0);
            Element fstElmnt = (Element) Keynode;
            fstElmnt.setAttribute("value", SHA1);//set the value of new edited ip here
            String newNodeValue =   fstElmnt.getAttribute("value");
            Log.d("SHA1", newNodeValue);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            // initialize StreamResult with File object to save to file
            StreamResult result = new StreamResult(file1);
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);

            Log.d("SHA1", newNodeValue);
        } catch (Exception e) {
            System.out.println("XML Parsing Excpetion = " + e);
        }*//*


        */
/*try {
            //Obtenemos la referencia al fichero XML de entrada
            FileInputStream fil = context.openFileInput("test.xml");

//DOM (Por ejemplo)
            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(fil);

//A partir de aquí se trataría el árbol DOM como siempre.
//Por ejemplo:
            Element root = dom.getDocumentElement();

            // Change the content of node
            Node nodes = dom.getElementsByTagName("DigestValue").item(0);
            // I changed the below line form nodes.setNodeValue to nodes.setTextContent
            nodes.setTextContent(SHA1);
            Log.d("SHA1 - TEXTO ", SHA1);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            *//*
*/
/*File roott = Environment.getExternalStorageDirectory();
            File filee = new File(roott, "factura/test.xml");*//*
*/
/*

            FileOutputStream file= context.openFileOutput("test2.xml", MODE_PRIVATE);
            // initialize StreamResult with File object to save to file
            StreamResult result = new StreamResult(file);
            DOMSource source = new DOMSource(dom);
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }*//*



        */
/*//*
/Obtenemos la referencia al fichero XML de entrada
        FileInputStream fil = context.openFileInput("prueba.xml");

//DOM (Por ejemplo)
        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document dom = builder.parse(fil);

//A partir de aquí se trataría el árbol DOM como siempre.
//Por ejemplo:
        Element root = dom.getDocumentElement();*//*



        }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
