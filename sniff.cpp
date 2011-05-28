#include<iostream>
#include<pcap.h>
#include<assert.h>
using namespace std;
int main(int argc, char *argv[])
{
	char *dev,errbuf[PCAP_ERRBUF_SIZE];
	dev= pcap_lookupdev(errbuf);
	assert(dev!=NULL);
	cout <<"Device: "<<dev<<endl;
	pcap_t *handle;
	handle=pcap_open_live(dev,BUFSIZ,1,10000,errbuf);
	assert(handle!=NULL);
	return(0);
}
