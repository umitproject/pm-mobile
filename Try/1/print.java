class print{
	public native String prints();
	public static void main(String args[]){
		print a= new print();
		String b=a.prints();
		System.out.println(b);
	}
	static{
		System.loadLibrary("Native");
	}
}
