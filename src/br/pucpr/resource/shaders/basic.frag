#version 330

//Cor a ser pintada, recebida do vertex shader
//Não se trata exatamente do mesmo valor. Quando um pixel no meio do triangulo for pintado, suas cores serão misturadas
//proporcionalmente a distância de cada vértice.
in vec4 vColor;


//Cor de saída, que será desenhada na tela.
out vec4 outColor;

void main() {
    outColor = vColor;
}