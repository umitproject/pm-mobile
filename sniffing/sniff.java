class sniff{
	public native int Sniff();
	public static void main(String args[]){
		int ret=new sniff().Sniff();
		System.out.println(ret);
	}
	static{
		System.loadLibrary("sniff");
	}
}
