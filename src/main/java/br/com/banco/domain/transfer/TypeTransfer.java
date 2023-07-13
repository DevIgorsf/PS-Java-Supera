package br.com.banco.domain.transfer;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum TypeTransfer {

    DEPOSIT("deposíto"),
    ENTRY_TRANSFER("Transferência Entrada"),
    OUTBOUND_TRANSFER("Transferência Saída"),
    WITHDRAW("Saque");
    private String description;

    TypeTransfer(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static final Map<String, TypeTransfer> typeTransferMap = new HashMap<String, TypeTransfer>();
    static {
        for (TypeTransfer typeTransfer : EnumSet.allOf(TypeTransfer.class)) {
            typeTransferMap.put(typeTransfer.getDescription(), typeTransfer);
        }
    }

    public static TypeTransfer get(String string) {
        return typeTransferMap.get(string);
    }

}
