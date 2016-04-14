
import java.io.Serializable;

/**
 * 
 * CS 342 Spring 2016
 * Shen Wang (swang224@uic.edu), Yunxiao Cui (ycui22@uic.edu), Yue Yu (yyu31@uic.edu)
 * 
 * message class
 * represent the object used to communicate among server and client 
 */

public class Message implements Serializable{
    
    private static final long serialVersionUID = 1L;
    public String type, sender, content, recipient;
    
    public Message(String type, String sender, String content, String recipient){
        this.type = type; 
        this.sender = sender; 
        this.content = content; 
        this.recipient = recipient;
    }
    
    @Override
    public String toString(){
        return "{type='"+type+"', sender='"+sender+"', content='"+content+"', recipient='"+recipient+"'}";
    }
}
