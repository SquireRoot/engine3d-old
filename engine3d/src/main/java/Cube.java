import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.*;

public class Cube implements Drawable {
    private Mesh playerMesh;
    private int shaderID;

    private int samplerID;
    private int mvpID;

    private Matrix4x4 position;

    Cube() {
        playerMesh = new Mesh();
        position = Matrix4x4.translation(new Vector3(0f,0f,0));
    }

    public void draw(Matrix4x4 vp) {
        glUseProgram(shaderID);

        Matrix4x4 mvp = Matrix4x4.scale(new Vector3(0.5f, 0.5f, 1));
        //Matrix4x4 mvp = position.times(vp);
        glUniformMatrix4fv(mvpID,true, mvp.matrix);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, playerMesh.textureID);
        glUniform1i(samplerID, 0);

        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, playerMesh.vertexBufferID);
        glVertexAttribPointer(0,3, GL_FLOAT,false,0, 0);

        glEnableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER, playerMesh.uvBufferID);
        glVertexAttribPointer(1,2,GL_FLOAT, false, 0,0);

        glDrawArrays(GL_TRIANGLES, 0, playerMesh.getNumTris());
    }

    public void load() {
        playerMesh.loadData();
        shaderID = GraphicsSystem.compileShaders("VertexShader.shader",
                                            "FragmentShader.shader");
        samplerID = glGetUniformLocation(shaderID, "sampler");
        if (samplerID == -1) {
            GameEngine.writeLogLn("Warning: sampler variable could not be found");
        }
        mvpID = glGetUniformLocation(shaderID, "MVP");
        if (mvpID == -1) {
            GameEngine.writeLogLn("Warning: mvp variable could not be found");
        }
    }
}