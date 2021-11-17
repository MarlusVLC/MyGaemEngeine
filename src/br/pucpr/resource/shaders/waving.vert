#version 330

const float a = 0.125; //amplitude
const float b = 4; //frequencia
const float h = 0; //deslocamento horizontal
const float k = 0;
const float PI = 3.14159;
//Atributos do vértice: posição e cor
//São variáveis de entrada do shader, portanto, devem ser associadas a buffers pelo java
in vec3 aPosition;
in vec3 aColor;

//Matriz de transformação World. Deve ser carregada pelo Java.
uniform mat4 uWorld = mat4(1.0);

//Matrizes de transformação da câmera
uniform mat4 uView = mat4(1.0);       //Posicionamento
uniform mat4 uProjection  = mat4(1.0); //Abertura

uniform float time;


//Variável de saída, para repassar a cor para o fragment shader
out vec4 vColor;
out float vDepth;

void main(){
    float distance = length(aPosition);
//    float z = amplitude*atan(-PI*distance*frequency+time);
//    float origFunc = distance*distance;
    float func = sin(-PI*distance*b+time);
    float z = a*func;
    float normalizedFunc = (func+1)/2;
    //Transforma a posição do triangulo coordenadas do modelo para coordenadas do mundo
    gl_Position =  uProjection * uView * uWorld * vec4(aPosition.xy, z, 1.0);

    //Repassar a cor do vértice para o fragment shader. Acrescenta um alfa de 1.0.
//    vColor = vec4(aColor, 1.0);
    vColor = vec4(normalizedFunc*aColor.x,normalizedFunc*aColor.y,normalizedFunc*aColor.z, 1.0);
}