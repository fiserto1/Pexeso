/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pexeso;

import pexeso.delegates.MessageDelegate;

import java.io.Serializable;

/**
 * Trida pro zpravu, ktera se pres delegata vypisuje kam je potreba.
 * 
 * @author Tomas
 */
public class Message implements Serializable {

    /**
     * Zprava pro vyjimky a nepriznive situace.
     */
    private String errorMessage;
    /**
     * Hlavni zprava. Informuje uzivatele co by mel delat.
     */
    private String headMessage;
    private transient MessageDelegate delegate;

    /**
     * Konstruktor s parametrem pro delegata.
     * @param delegate Delegat pro vypis zpravy.
     */
    public Message(MessageDelegate delegate) {
        this.delegate = delegate;
    }

    /**
     * Nastavi nepriznivou zpravu, delegatovi oznami ze byla zprava zmenena.
     * @param errorMessage Neprizniva zprava.
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        if (delegate != null) {
            delegate.errorMessageChanged(this);
        }
    }

    /**
     * Nastavi priznivou zpravu, delegatovi oznami ze byla zprava zmenena.
     * @param headMessage Prizniva zprava.
     */
    public void setHeadMessage(String headMessage) {
        this.headMessage = headMessage;
        if (delegate != null) {
            delegate.headMessageChanged(this);
        }
    }

    /**
     * Nastavi delegata, kteremu je oznamovana zmena zprav.
     * @param delegate Delegat pro vypis zpravy.
     */
    public void setDelegate(MessageDelegate delegate) {
        this.delegate = delegate;
    }

    /**
     * 
     * @return Vrati nepriznivou zpravu.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * 
     * @return Vrati priznivou zpravu.
     */
    public String getHeadMessage() {
        return headMessage;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.errorMessage != null ? this.errorMessage.hashCode() : 0);
        hash = 37 * hash + (this.headMessage != null ? this.headMessage.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Message other = (Message) obj;
        if ((this.errorMessage == null) ? (other.errorMessage != null) : !this.errorMessage.equals(other.errorMessage)) {
            return false;
        }
        if ((this.headMessage == null) ? (other.headMessage != null) : !this.headMessage.equals(other.headMessage)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Message{" + "errorMessage=" + errorMessage + ", headMessage=" + headMessage + ", delegate=" + delegate + '}';
    }
}
