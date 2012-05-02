 #include <pcap.h>
 #include <stdio.h>
 #include <string.h>
 #include <stdlib.h>
 #include <ctype.h>
 #include <errno.h>
 #include <sys/types.h>
 #include <sys/socket.h>
 #include <netinet/in.h>
 #include <arpa/inet.h>


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

char *out;


void
print_hex_ascii_line(const u_char *payload, int len, int offset)
{

	int i;
	int gap;
	const u_char *ch;


	printf("%05d   ", offset);
	

	ch = payload;
	for(i = 0; i < len; i++) {
		printf("%02x ", *ch);
		ch++;

		if (i == 7)
			printf(" ");
	}

	if (len < 8)
		printf(" ");
	

	if (len < 16) {
		gap = 16 - len;
		for (i = 0; i < gap; i++) {
			printf("   ");
		}
	}
	printf("   ");
	

	ch = payload;
	for(i = 0; i < len; i++) {
		if (isprint(*ch))
			printf("%c", *ch);
		else
			printf(".");
		ch++;
	}

	printf("\n");

return;
}



void print_payload(const u_char *payload, int len)
{
	/*	int len_rem=len;
		int line_width=16;	// Width of payload(fixed)
		int line_len;
		int offset=0;
		const u_char *ch = payload;
		if(len<=0)
		return;
		else if(len<=line_width)
		{
		print_hex_ascii_line(ch,len,offset);
		return;
		}
		while(1)
		{
		line_len = line_width % len_rem;

		print_hex_ascii_line(ch,line_len,offset);

		len_rem=len_rem - line_len;

		ch=ch+line_len;

		offset = offset +line_width;

		if(len_rem <=line_width){
		print_hex_ascii_line(ch,len_rem, offset);
		break;
		}
		}*/
	const u_char *ch = payload;
	int i;

	if (len <= 0)
		return;

	int mode = 0;

	char tmp =*ch;
	for(i=0;i<len;i++)
	{
		temp=*(ch+i);
		printf("%c",temp);
	}
/*	tmp[0] = *ch;
	tmp[1] = *(ch + 1);
	tmp[2] = *(ch + 2);
	tmp[3] = *(ch + 3);
	tmp[4] = '\0';

	if (truncated > 0
			&& !(tmp[0] == 'H' && tmp[1] == 'T' && tmp[2] == 'T' && tmp[3] == 'P')
			&& !(tmp[0] == 'G' && tmp[1] == 'E' && tmp[2] == 'T')) {
		mode = 7;
	} else {
		if (!(tmp[0] == 'G' && tmp[1] == 'E' && tmp[2] == 'T')) {
			return;
		}
		host_idx = 0;
		cookie_idx = 0;
		host[host_idx] = '\0';
		cookie[cookie_idx] = '\0';
		truncated = 0;
	}

	for (i = 0; i <= len; i++) {
		char c = *ch;

		if (mode == 0 && (c == 'C' || c == 'c')) {
			mode = 1;
			truncated = 0;
		} else if (mode == 1 && (c == 'o')) {
			mode = 2;
			truncated = 0;
		} else if (mode == 2 && (c == 'o')) {
			mode = 3;
			truncated = 0;
		} else if (mode == 3 && (c == 'k')) {
			mode = 4;
			truncated = 0;
		} else if (mode == 4 && (c == 'i')) {
			mode = 5;
			truncated = 0;
		} else if (mode == 5 && (c == 'e')) {
			mode = 6;
			truncated = 0;
		} else if (mode == 6 && (c == ':')) {
			mode = 7;
		} else if (mode == 7 && c == '\n') {
			cookie[cookie_idx] = '\0';
			host[host_idx] = '\0';
			printf("Cookie:%s|||Host=%s|||IP=%s\n", cookie, host, inet_ntoa(ip->ip_src));
			fflush(stdout);
			truncated = 0;
			mode = 0;
		} else if (mode == 15 && c == '\n') {
			mode = 0;
		} else if (mode == 0 && (c == 'H' || c == 'h')) {
			mode = 11;
		} else if (mode == 11 && (c == 'o')) {
			mode = 12;
		} else if (mode == 12 && (c == 's')) {
			mode = 13;
		} else if (mode == 13 && (c == 't')) {
			mode = 14;
		} else if (mode == 14 && (c == ':')) {
			mode = 15;
			i++; // remove space
		} else if (mode == 7) {
			if (isprint(c)) {
				cookie[cookie_idx] = c;
				if (cookie_idx < sizeof(cookie)) {
					cookie_idx++;
				} else {
					printf("!!!OVERFLOW!!!");
					fflush(stdout);
				}
			}
		} else if (mode == 15) {
			if (isprint(c)) {
				host[host_idx] = c;
				if (host_idx < sizeof(host)) {
					host_idx++;
				} else {
					printf("!!!OVERFLOW!!!");
					fflush(stdout);
				}
			}
		} else {
			mode = 0;
		}
		ch++;
	}
	if (mode == 7) {
		truncated = 1;
	}*/
	return;
}



void callback(u_char *args,const struct pcap_pkthdr *header, const u_char *packet)
{

	printf("call back called!!\n");
	const struct sniff_ethernet *ethernet;	/*ethernet header*/
	const struct sniff_ip *ip;		/*ip header*/
	const struct sniff_tcp *tcp;		/*tcp header*/
	const char *payload;

	int size_ip;		/*size of ip header*/
	int size_tcp;		/*size of tcp header*/
	int size_payload;	/*size of payload*/

	/*fetching headers and their sizes*/
	ethernet =(struct sniff_ethernet*)(packet);
	ip=(struct sniff_ip*)(packet+SIZE_ETHERNET);
	size_ip=IP_HL(ip)*4;

	/*fetching data from headers*/
	printf("Source : %s   Desination: %s\n",inet_ntoa(ip->ip_src),inet_ntoa(ip->ip_dst));

	/*getting the packet type*/
	switch(ip->ip_p)
	{
		case IPPROTO_TCP:
			printf("Protocol:TCP\n");
			break;
		case IPPROTO_UDP:
			printf("Protocol:UDP\n");
			return;
		case IPPROTO_ICMP:
			printf("Protocol:ICMP\n");
			return;
		case IPPROTO_IP:
			printf("Protocol:IP\n");
			return;
		default:
			printf("Unknown Protocol: o.O\n");
			return;
	}
	tcp=(struct sniff_tcp*)(packet + SIZE_ETHERNET + size_ip);
	size_tcp=TH_OFF(tcp)*4;
	if(size_tcp<20){
		printf("Invalid TCP header length: %d bytes\n",size_tcp);
		return;
	}
	printf("Source port: %d\n",ntohs(tcp->th_sport));
	printf("Destination port: %d\n",ntohs(tcp->th_dport));

	/* fetching payload*/
	payload = (u_char *)(packet + SIZE_ETHERNET + size_ip + size_tcp);
	size_payload=ntohs(ip->ip_len)-(size_ip+size_tcp);

	if(size_payload>0){
		printf("Payload(%d)bytes\n",size_payload);
		print_payload(payload,size_payload);
	}
}
int sniff()
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
		printf("Couldn't find default device: %s\n",errbuf);
		return(2);
	}
	else{
		printf("Device :%s\n",dev);;
		printf("Filter Expression : %s\n",filter_exp);
	}
	/* Find the properties for the device */
	if (pcap_lookupnet(dev, &net, &mask, errbuf) == -1) {
		printf("Couldn't get netmask for device %s: %s\n",dev,errbuf);
		net = 0;
		mask = 0;
	}
	/* Open the session in promiscuous mode */
	handle = pcap_open_live(dev, BUFSIZ, 1, 1000, errbuf);
	if (handle == NULL) {
		printf("Couldn't open device %s : %s\n",dev,errbuf);;
		return(2);
	}
	/* Compile and apply the filter */
	if (pcap_compile(handle, &fp, filter_exp, 0, net) == -1) {
		printf("Couldn't parse filter %s: %s \n",filter_exp, pcap_geterr(handle));;
		return(2);
	}
	if (pcap_setfilter(handle, &fp) == -1) {
		printf("Couldn't install filter %s: %s\n",filter_exp,pcap_geterr(handle));
		return(2);
	}
	/* Grab packets */
	printf("pcap_loop will be called\n");;
	packet = pcap_next(handle, &header);
	/* Print its length */
	printf("Jacked a packet with length of [%d]\n", header.len);
	pcap_loop(handle,0,callback,NULL);
	printf("pcap_loop done\n");
	/* And close the session */
	pcap_close(handle);
	pcap_freecode(&fp);
	return(0);
}
int main(int argv,char **argc)
{
	FILE *fp;
	fp=fopen("text_native","w");
	fprintf(fp,"native was executed");
	int r;
	r=sniff();
	return 0;
}
