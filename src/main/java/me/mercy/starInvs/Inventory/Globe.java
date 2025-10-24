package me.mercy.starInvs.Inventory;

import java.util.List;

public class Globe {

    private int slot = 0;
    private boolean type = false;
    private List<String> list = null;

    public Globe(int Slot, boolean Type, List<String> List) {
        this.slot = Slot;
        this.type = Type;
        this.list = List;
    }

    public void clear(){
        slot = 0;
        type = false;
        list = null;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
