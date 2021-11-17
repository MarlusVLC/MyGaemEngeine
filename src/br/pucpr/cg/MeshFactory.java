package br.pucpr.cg;

import br.pucpr.Util;
import br.pucpr.mage.Reader;
import br.pucpr.mage.Shader;
import org.joml.Vector3f;

import br.pucpr.mage.Mesh;
import br.pucpr.mage.MeshBuilder;

public class MeshFactory {
    private Shader shader;
    private boolean wireframeMode = false;

    public MeshFactory(Shader shader){
        this.shader = shader;
    }

    public MeshFactory(String shaderName){
        this.shader = Shader.loadProgram(shaderName);
    }

    public MeshFactory(){
        this.shader = Shader.loadProgram("basic");
    }

    public Mesh createCube() {
        return createCube(
                new Vector3f(0.988f, 0.663f, 0.522f),
                new Vector3f(0.522f, 0.792f, 0.365f),
                new Vector3f(0.459f, 0.537f, 0.749f),
                new Vector3f(0.976f, 0.549f, 0.714f),
                new Vector3f(0.647f, 0.537f, 0.757f),
                new Vector3f(0.753f, 0.729f, 0.600f)
        );
    }

    public Mesh createCube(Vector3f color){
        return createCube(color, color, color, color, color, color);
    }

    public Mesh createCube(Vector3f frontColor, Vector3f backColor, Vector3f topColor, Vector3f bottomColor, Vector3f rightColor, Vector3f leftColor) {
        return new MeshBuilder(shader)
                .addVector3fAttribute("aPosition",
                        //Face próxima
                        -0.5f, 0.5f, 0.5f,  //0
                        0.5f, 0.5f, 0.5f,  //1
                        -0.5f, -0.5f, 0.5f,  //2
                        0.5f, -0.5f, 0.5f,  //3
                        //Face afastada
                        -0.5f, 0.5f, -0.5f,  //4
                        0.5f, 0.5f, -0.5f,  //5
                        -0.5f, -0.5f, -0.5f,  //6
                        0.5f, -0.5f, -0.5f,  //7
                        //Face superior
                        -0.5f, 0.5f, 0.5f,  //8
                        0.5f, 0.5f, 0.5f,  //9
                        -0.5f, 0.5f, -0.5f,  //10
                        0.5f, 0.5f, -0.5f,  //11
                        //Face inferior
                        -0.5f, -0.5f, 0.5f,  //12
                        0.5f, -0.5f, 0.5f,  //13
                        -0.5f, -0.5f, -0.5f,  //14
                        0.5f, -0.5f, -0.5f,  //15
                        //Face direita
                        0.5f, -0.5f, 0.5f,  //16
                        0.5f, 0.5f, 0.5f,  //17
                        0.5f, -0.5f, -0.5f,  //18
                        0.5f, 0.5f, -0.5f,  //19
                        //Face esquerda
                        -0.5f, -0.5f, 0.5f,   //20
                        -0.5f, 0.5f, 0.5f,   //21
                        -0.5f, -0.5f, -0.5f,  //22
                        -0.5f, 0.5f, -0.5f)  //23
                .addVector3fAttribute("aColor",
                        //Face próxima
                        frontColor,
                        frontColor,
                        frontColor,
                        frontColor,
                        //Face afastada
                        backColor,
                        backColor,
                        backColor,
                        backColor,
                        //Face superior
                        topColor,
                        topColor,
                        topColor,
                        topColor,
                        //Face inferior
                        bottomColor,
                        bottomColor,
                        bottomColor,
                        bottomColor,
                        //Face direita
                        rightColor,
                        rightColor,
                        rightColor,
                        rightColor,
                        //Face esquerda
                        leftColor,
                        leftColor,
                        leftColor,
                        leftColor)
                .setIndexBuffer(
                        //Face próxima
                        0, 2, 3,
                        0, 3, 1,
                        //Face afastada
                        4, 7, 6,
                        4, 5, 7,
                        //Face superior
                        8, 11, 10,
                        8, 9, 11,
                        //Face inferior
                        12, 14, 15,
                        12, 15, 13,
                        //Face direita
                        16, 18, 19,
                        16, 19, 17,
                        //Face esquerda
                        20, 23, 22,
                        20, 21, 23)
                .create();
    }

    public Mesh createPlane(int depth, int width, float scale, Vector3f color){
        var vertexAmount = depth*width;
        var vertexData = new Vector3f[vertexAmount];
        float halfWidth = (width-1)*scale/2;
        float halfDepth = (depth-1)*scale/2;

        var i = 0;
        for (int w = 0; w < width; w++) {
            for (int d = 0; d < depth; d++) {
                vertexData[i] = new Vector3f(w * scale - halfWidth, d * scale - halfDepth, 0.0f);
                i++;
            }
        }

        var rectAmount = (depth-1)*(width-1);
        var indexAmount = 6*rectAmount;
        var indexData_vertex = new int[indexAmount];

        var currBaseVertex = 0;
        boolean isNearbyEdge;
        for (i = 0; i < indexAmount; i+=6){
            indexData_vertex[i] = currBaseVertex;
            indexData_vertex[i+1] = currBaseVertex+depth;
            indexData_vertex[i+2] = currBaseVertex+1;
            indexData_vertex[i+3] = currBaseVertex+depth;
            indexData_vertex[i+4] = currBaseVertex+1+depth;
            indexData_vertex[i+5] = currBaseVertex+1;
            isNearbyEdge = (currBaseVertex+2)%depth == 0;
            currBaseVertex += isNearbyEdge ? 2 : 1;
        }

        var colorData = new Vector3f[vertexAmount];
        for (i = 0; i < vertexAmount; i++){
            colorData[i] = color;
        }

        return new MeshBuilder(shader)
                .addVector3fAttribute("aPosition", vertexData)
                .addVector3fAttribute("aColor", colorData)
                .setIndexBuffer(indexData_vertex)
                .create()
                .setWireframe(wireframeMode);
    }

    public Mesh createHeightMap(String referenceImagePath, float scale, float heightScale, Vector3f color){
        var file = Reader.findInputStream(referenceImagePath);
        var referenceImage = Reader.getImageFromInputStream(file);
        var vertexDepth = referenceImage.getHeight();
        var vertexWidth = referenceImage.getWidth();

        var vertexAmount = vertexDepth*vertexWidth;
        var vertexData = new Vector3f[vertexAmount];
        var vertexHeight = new float[vertexAmount];
        float halfWidth = (vertexWidth-1)*scale/2;
        float halfDepth = (vertexDepth-1)*scale/2;

        var i = 0;
        for (int w = 0; w < vertexWidth; w++){
            for (int d = 0; d < vertexDepth; d++){
                vertexHeight[i] = Util.weightedGrayscale(Util.fromRGB(referenceImage.getRGB(w,d)));
                vertexHeight[i] *= heightScale;
                vertexData[i] = new Vector3f(w*scale-halfWidth,d*scale-halfDepth,-vertexHeight[i]);
                i++;
            }
        }

        var rectAmount = (vertexDepth-1)*(vertexWidth-1);
        var indexAmount = 6*rectAmount;
        var indexData_vertex = new int[indexAmount];

        var currBaseVertex = 0;
        boolean isNearbyEdge;
        for (i = 0; i < indexAmount; i+=6){
            indexData_vertex[i+5] = currBaseVertex;
            indexData_vertex[i+4] = currBaseVertex+vertexDepth;
            indexData_vertex[i+3] = currBaseVertex+1;
            indexData_vertex[i+2] = currBaseVertex+vertexDepth;
            indexData_vertex[i+1] = currBaseVertex+1+vertexDepth;
            indexData_vertex[i] = currBaseVertex+1;
            isNearbyEdge = (currBaseVertex+2)%vertexDepth == 0;
            currBaseVertex += isNearbyEdge ? 2 : 1;
        }

        var colorData = new Vector3f[vertexAmount];
        for (i = 0; i < vertexAmount; i++){
            colorData[i] = Util.multiply(color,vertexHeight[i]*0.125f/(scale*2f));
        }

        return new MeshBuilder(shader)
                .addVector3fAttribute("aPosition", vertexData)
                .addVector3fAttribute("aColor", colorData)
                .setIndexBuffer(indexData_vertex)
                .create()
                .setWireframe(wireframeMode);
    }

    public boolean isWireframeMode() {
        return wireframeMode;
    }

    public MeshFactory setWireframeMode(boolean wireframeMode) {
        this.wireframeMode = wireframeMode;
        return this;
    }
}
