package bg.sofia.uni.fmi.mjt.shopping;

class ItemNotFoundException extends Exception {

    ItemNotFoundException() {
        super("Item does not exist");
    }

}
