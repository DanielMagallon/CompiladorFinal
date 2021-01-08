#include <stdio.h>
#include <stdlib.h>
#include <string.h>

char temp;
int main()
{
	char x;
		char *y;
		printf("%s\n","Dame un caracter: ");
	scanf(" %c",&x);
	printf("%s\n","Dame una cadena: ");
	y = (char *) malloc(sizeof(char));
scanf(" %[^\n]%*c",y);
	printf( "Introdujo: %c y %s\n" , x,y);
	printf("%s\n","Dame una cadena: ");
	y = (char *) malloc(sizeof(char));
scanf(" %[^\n]%*c",y);
	printf( "Introdujo: %s\n" , y);
	printf("%s\n","Dame un caracter: ");
	scanf(" %c",&x);
	printf( "Introdujo: %c\n" , x);
	printf("%s\n","Dame un caracter: ");
	scanf(" %c",&x);
	printf( "Introdujo: %c\n" , x);

printf("Presione enter para salir: ");
scanf("%c",&temp);
scanf("%c",&temp);
}