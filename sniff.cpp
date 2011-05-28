#include<iostream>
#include<pcap.h>
#include<assert.h>
#include<netinet/in.h>
#include<arpa/inet.h>

#define SIZE_ETHERNET 14
#define ETHER_ADDR_LEN 6

/* Ethernet header */
struct sniff_ethernet {
        u_char  ether_dhost[ETHER_ADDR_LEN];    /* destination host address */
        u_char  ether_shost[ETHER_ADDR_LEN];    /* source host address */
        u_short ether_type;                     /* IP? ARP? RARP? etc */
};

/* IP header */
struct sniff_ip {
        u_char  ip_vhl;                 /* version << 4 | header length >> 2 */
        u_char  ip_tos;                 /* type of service */
        u_short ip_len;                 /* total length */
        u_short ip_id;                  /* identification */
        u_short ip_off;                 /* fragment offset field */
        #define IP_RF 0x8000            /* reserved fragment flag */
        #define IP_DF 0x4000            /* dont fragment flag */
        #define IP_MF 0x2000            /* more fragments flag */
        #define IP_OFFMASK 0x1fff       /* mask for fragmenting bits */
        u_char  ip_ttl;                 /* time to live */
        u_char  ip_p;                   /* protocol */
        u_short ip_sum;                 /* checksum */
        struct  in_addr ip_src,ip_dst;  /* source and dest address */
};
#define IP_HL(ip)               (((ip)->ip_vhl) & 0x0f)
#define IP_V(ip)                (((ip)->ip_vhl) >> 4)

/* TCP header */
typedef u_int tcp_seq;

struct sniff_tcp {
        u_short th_sport;               /* source port */
        u_short th_dport;               /* destination port */
        tcp_seq th_seq;                 /* sequence number */
        tcp_seq th_ack;                 /* acknowledgement number */
        u_char  th_offx2;               /* data offset, rsvd */
#define TH_OFF(th)      (((th)->th_offx2 & 0xf0) >> 4)
        u_char  th_flags;
        #define TH_FIN  0x01
        #define TH_SYN  0x02
        #define TH_RST  0x04
        #define TH_PUSH 0x08
        #define TH_ACK  0x10
        #define TH_URG  0x20
        #define TH_ECE  0x40
        #define TH_CWR  0x80
        #define TH_FLAGS        (TH_FIN|TH_SYN|TH_RST|TH_ACK|TH_URG|TH_ECE|TH_CWR)
        u_short th_win;                 /* window */
        u_short th_sum;                 /* checksum */
        u_short th_urp;                 /* urgent pointer */
};



using namespace std;



void callback(u_char *args,const struct pcap_pkthdr *header, const u_char *packet)
{

	cout<<"call back called!!"<<endl;
	const struct sniff_ethernet *ethernet;	/*ethernet header*/
	const struct sniff_ip *ip;		/*ip header*/
	const struct sniff_tcp *tcp;		/*tcp header*/

	int size_ip;		/*size of ip header*/
	int size_tcp;		/*size of tcp header*/
	int size_payload;	/*size of payload*/

	/*fetching headers and their sizes*/
	ethernet =(struct sniff_ethernet*)(packet);
	ip=(struct sniff_ip*)(packet+SIZE_ETHERNET);
	size_ip=IP_HL(ip)*4;
	tcp=(struct sniff_tcp*)(packet + SIZE_ETHERNET + size_ip);
	size_tcp=TH_OFF(tcp)*4;


	/*fetching data from headers*/
	cout<<"From : "<<inet_ntoa(ip->ip_src)<<endl;
	cout<<"To : "<<inet_ntoa(ip->ip_dst)<<endl;
}
int main(int argc, char *argv[])
{
	pcap_t *handle;			/* Session handle */
	char *dev;			/* The device to sniff on */
	char errbuf[PCAP_ERRBUF_SIZE];	/* Error string */
	struct bpf_program fp;		/* The compiled filter */
	char filter_exp[] = "";		/* The filter expression */
	bpf_u_int32 mask;		/* Our netmask */
	bpf_u_int32 net;		/* Our IP */
	struct pcap_pkthdr header;	/* The header that pcap gives us */
	const u_char *packet;		/* The actual packet */
//	struct in_addr addr;		/*Address*/

	/* Define the device */
	dev = pcap_lookupdev(errbuf);
	if (dev == NULL) {
		cout <<"Couldn't find default device:" << errbuf<<endl;
		return(2);
	}
	else{
		cout<<"Device :"<<dev<<endl;
		cout<<"Filter Expression :"<<filter_exp<<endl;
	}
	/* Find the properties for the device */
	if (pcap_lookupnet(dev, &net, &mask, errbuf) == -1) {
		cout<<"Couldn't get netmask for device "<<dev<<" : "<<errbuf<<endl;
		net = 0;
		mask = 0;
	}
	/* Open the session in promiscuous mode */
	handle = pcap_open_live(dev, BUFSIZ, 1, 1000, errbuf);
	if (handle == NULL) {
		cout<<"Couldn't open device "<< dev<<" : "<<errbuf<<endl;
		return(2);
	}
	/* Compile and apply the filter */
	if (pcap_compile(handle, &fp, filter_exp, 0, net) == -1) {
		cout<<"Couldn't parse filter "<<filter_exp<<" :"<< pcap_geterr(handle)<<endl;
		return(2);
	}
	if (pcap_setfilter(handle, &fp) == -1) {
		cout<<"Couldn't install filter "<<filter_exp<<" :"<<pcap_geterr(handle)<<endl;
		return(2);
	}
	/* Grab packets */
	cout<<"pcap_loop will be called"<<endl;
	pcap_loop(handle,-1,callback,NULL);
	cout<<"pcap_loop done"<<endl;
	/* And close the session */
	pcap_close(handle);
	pcap_freecode(&fp);
	return(0);
}
