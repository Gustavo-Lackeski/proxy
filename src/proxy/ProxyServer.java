/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package proxy;

import java.io.*; 
import java.net.*; 
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

class proxyServ { 
    private URL url; private URLConnection connect;
    String urlAconectar;
    //com o server
    Socket soc; InputStream is; 
    //com o cliente
    Socket soc2; OutputStream os2; BufferedReader is2;

    proxyServ(Socket s) throws IOException {        
        soc2 = s; 
        os2 = soc2.getOutputStream(); 
        is2 = new BufferedReader(new InputStreamReader(soc.getInputStream()));
        
    }
    //pega a resposta do server
    void getResponse() { 
        if(connect.getContentLength() > 0){
            try {
                is = connect.getInputStream();
            } catch (IOException ex) {
                Logger.getLogger(proxyServ.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    //retorna resposta ao cliente
    void returnResponse() { 
        int c; 
        try { 
            FileInputStream f = new FileInputStream("."+urlAconectar); 
            while ((c = f.read()) != -1) 
                os2.write(c) ; 
            f.close(); 
        } catch (IOException e) { 
        System.err.println("IOException in reading in Web server"); 
        } 

    }
    //faz request ao server
    void request(){
        try { 
            url = new URL(urlAconectar);
            connect = url.openConnection();
            connect.setDoInput(true);
            connect.setDoOutput(false);
         //   String message = "GET" + path + "\n\n"; 
       //     os.write(message.getBytes()); 
       //     os.flush(); 
        } catch (IOException e) { 
            System.err.println("Error in HTTP request"); 
        }
    }
    public void close() { 
        try { 
            is.close();           
            soc.close(); 
            is2.close();
            os2.close();
            soc.close();
        } catch (IOException e) { 
        System.err.println("IOException in closing connection"); 
        } 
    }
    //pega request do cliente
    void getRequest() { 
        try { 
            String message; 
            while ((message = is2.readLine()) != null) { 
                if (message.equals("")) 
                 break;
                System.err.println(message); 
                StringTokenizer t = new StringTokenizer(message);                 
                String token = t.nextToken(); // get first token 
                if (token.equals("GET")) // if token is ”GET” 
                     urlAconectar = t.nextToken(); // context a URl a conectar 
            }
        } catch (IOException e) { 
            System.err.println("Error receiving Web request"); 
        } 
    }
    
    public static void main(String args[]) {

        try { 
            ServerSocket s = new ServerSocket(8080); 
            for (;;) { 
                proxyServ w = new proxyServ(s.accept()); 
                w.getRequest(); 
                w.request();
                w.getResponse();
                w.returnResponse(); 
                w.close(); 
            } 

        } catch (IOException i) { 

        System.err.println("IOException in Server"); 

        } 

    }

}
