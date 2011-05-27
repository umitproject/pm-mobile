class Helloworld{
	public native void print();
	public static void main(String args[]){
		new Helloworld().print();
	}
	static{
//		System.load("/home/d34th4ck3r/Documents/UMIT/USOC/Try/libHello.so");
		System.loadLibrary("Hello");
	}
}
