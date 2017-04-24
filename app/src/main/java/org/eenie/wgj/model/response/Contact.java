package org.eenie.wgj.model.response;


/**
 * @author zjh
 * @date 2016/9/9
 */
public class Contact implements Comparable{
    private String firstAlphabet;
    private String name;

    public Contact(String name) {
        setName(name);
    }

    public String getFirstAlphabet() {
        return firstAlphabet;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

    }

    @Override
    public int compareTo(Object another) {
        Contact compareContact = (Contact) another;
        if (compareContact.getFirstAlphabet().equals("#")){
            return 1;
        }else if (getFirstAlphabet().equals("#")){
            return -1;
        }else {
            return getFirstAlphabet().compareTo(((Contact) another).getFirstAlphabet());
        }
    }
}
