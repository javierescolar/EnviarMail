package enviarmail;

import java.io.File;
import java.util.Hashtable;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EnviarMail {

    public static void main(String[] args) {
        String rutaFicheros = "C:\\Users\\javie\\Desktop\\expediciones";
        //listado de direcciones y nombre de sus ficheros
        Hashtable<String, String> direcciones = new Hashtable<String, String>();
        direcciones.put("javi.txt", "javierescolar10@gmail.com");
        direcciones.put("meli.txt", "melisalcedo59@gmail.com");

        File dir = new File(rutaFicheros);
        String[] ficheros = dir.list();
        if (ficheros == null) {
            System.out.println("No hay ficheros en el directorio especificado");
        } else {
            for (int x = 0; x < ficheros.length; x++) {
                sendMail(direcciones.get(ficheros[x]),rutaFicheros,ficheros[x]);
            }
        }
    }

    public static void sendMail(String direccion, String ruta, String fichero) {
        try {
            // se obtiene el objeto Session. La configuración es para
            // una cuenta de gmail.
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", "587");
            props.setProperty("mail.smtp.user", "pruebasparatodojavi@gmail.com");
            props.setProperty("mail.smtp.auth", "true");

            Session session = Session.getDefaultInstance(props, null);
            // session.setDebug(true);

            // Se compone la parte del texto
            BodyPart texto = new MimeBodyPart();
            texto.setText("Texto del mensaje");

            // Se compone el adjunto con la imagen
            BodyPart adjunto = new MimeBodyPart();
            adjunto.setDataHandler(
                    new DataHandler(new FileDataSource(ruta+"\\"+fichero)));
            adjunto.setFileName(fichero);

            // Una MultiParte para agrupar texto e imagen.
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto);
            multiParte.addBodyPart(adjunto);

            // Se compone el correo, dando to, from, subject y el
            // contenido.
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("pruebasparatodojavi@gmail.com"));
            message.addRecipient(
                    Message.RecipientType.TO,
                    new InternetAddress(direccion));
            message.setSubject("Expedicion");
            message.setContent(multiParte);

            // Se envia el correo.
            Transport t = session.getTransport("smtp");
            t.connect("pruebasparatodojavi@gmail.com", "pruebas123456");
            t.sendMessage(message, message.getAllRecipients());
            t.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
