# Aula 3 - Refatoração completa

Inclusão do conceito de malha poligonal (mesh). A malha divide-se em duas classes principais:

1. MeshBuilder: Utilizada para construção da malha
1. Mesh: Que representa a malha depois de pronta.

O código representa apenas malhas estáticas, cujos atributos não mudam após sua criação.

Adicionamos também ao Shader o método setUniformObject, que permite definir uniformes com base em sua classe. Isso 
permite armazenar um mapa de objetos na malha.

Observe em especial a criação e desenho (métodos init() e draw()), que agora ficam drasticamente simplificados.