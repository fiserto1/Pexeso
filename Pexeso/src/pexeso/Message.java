/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pexeso;

import java.io.Serializable;
import pexeso.delegates.MessageDelegate;

/**
 *
 * @author Tomas
 */
public class Message implements Serializable {

    private String errorMessage;
    private String headMessage;
    private transient MessageDelegate delegate;

    public Message(MessageDelegate delegate) {
        this.delegate = delegate;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        if (delegate != null) {
            delegate.errorMessageChanged(this);
        }
    }

    public void setHeadMessage(String headMessage) {
        this.headMessage = headMessage;
        if (delegate != null) {
            delegate.headMessageChanged(this);
        }
    }

    public void setDelegate(MessageDelegate delegate) {
        this.delegate = delegate;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getHeadMessage() {
        return headMessage;
    }
}
