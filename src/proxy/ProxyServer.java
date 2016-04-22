/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxy;

import java.io.*; 
import java.net.*; 
import java.util.StringTokenizer;

class proxyServer { 
    String resource;
    //com o server
    Socket soc; OutputStream os; InputStream is; 
    //com o cliente
    Socket soc2; OutputStream os2; DataInputStream is2;

 //.... 

    proxyServer(String server, int port, Socket s) 
                throws IOException, UnknownHostException { 
        //comunicacao com server
        soc = new Socket(server, port); 
        os = soc.getOutputStream();
        is = soc.getInputStream(); 
        //com o cliente
        soc2 = s; 
        os2 = soc2.getOutputStream(); 
        is2 = new DataInputStream(soc.getInputStream());
        
    }
    //pega a resposta do server
    void getResponse() { 

        int c; 
        try { 
            while ((c = is.read()) != -1) 
              System.out.print((char) c); 
        } catch (IOException e) { 
        System.err.println("IOException in reading from Web server"); 
        } 

    }
    //retorna resposta ao cliente
    void returnResponse() { 
        int c; 
        try { 
            FileInputStream f = new FileInputStream("."+resource); 
            while ((c = f.read()) != -1) 
                os2.write(c) ; 
            f.close(); 
        } catch (IOException e) { 
        System.err.println("IOException in reading in Web server"); 
        } 

    }
    //faz request ao server
    void request(String path){
        try { 
            String message = "GET" + path + "\n\n"; 
            os.write(message.getBytes()); 
            os.flush(); 
        } catch (IOException e) { 
        System.err.println("Error in HTTP request"); 
        }
    }
    public void close() { 
        try { 
            is.close(); 
            os.close(); 
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
                     resource = t.nextToken(); // get second token 
            }
        } catch (IOException e) { 
        System.err.println("Error receiving Web request"); 
        } 
    }
    
    public static void main(String args[]) {

        try { 
            ServerSocket s = new ServerSocket(8080); 
            for (;;) { 
                proxyServer w = new proxyServer("g1.globo.com", 80, s.accept()); 
                w.getRequest(); 
                w.request("/economia/mercados/index.html");
                w.getResponse();
                w.returnResponse(); 
                w.close(); 
                } 

        } catch (IOException i) { 

        System.err.println("IOException in Server"); 

        } 

    }

}
