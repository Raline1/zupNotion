package br.com.zup.ZupNotion.usuario;

import br.com.zup.ZupNotion.components.Conversor;
import br.com.zup.ZupNotion.config.security.JWT.JWTComponent;
import br.com.zup.ZupNotion.config.security.JWT.UsuarioLoginService;
import br.com.zup.ZupNotion.controllers.UsuarioController;
import br.com.zup.ZupNotion.exceptions.PerfilInvalidoException;
import br.com.zup.ZupNotion.exceptions.UsuarioNaoExisteException;
import br.com.zup.ZupNotion.models.Tarefa;
import br.com.zup.ZupNotion.models.Usuario;
import br.com.zup.ZupNotion.models.dtos.AlterarSenhaDTO;
import br.com.zup.ZupNotion.models.dtos.CadastroUsuarioDTO;
import br.com.zup.ZupNotion.models.dtos.InformarEmailDTO;
import br.com.zup.ZupNotion.services.EmailService;
import br.com.zup.ZupNotion.services.SenhaService;
import br.com.zup.ZupNotion.services.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({UsuarioController.class, Conversor.class, UsuarioLoginService.class, JWTComponent.class})
public class UsuarioControllerTest {

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private SenhaService senhaService;

    @MockBean
    private UsuarioLoginService usuarioLoginService;

    @MockBean
    private EmailService emailService;

    @MockBean
    private JWTComponent jwtComponent;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private Usuario usuario;
    private CadastroUsuarioDTO cadastroUsuarioDTO;
    private AlterarSenhaDTO alterarSenhaDTO;
    private InformarEmailDTO informarEmailDTO;
    private Page<Usuario> pageUsuario;
    private Pageable pageable;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();

        usuario = new Usuario();
        usuario.setId("123abc");
        usuario.setNome("Fulano");
        usuario.setEmail("fulano@zup.com.br");
        usuario.setSenha("CA@123we");

        cadastroUsuarioDTO = new CadastroUsuarioDTO();
        cadastroUsuarioDTO.setNome("Fulano");
        cadastroUsuarioDTO.setEmail("fulano@zup.com.br");
        cadastroUsuarioDTO.setSenha("Fulano@12");

        alterarSenhaDTO = new AlterarSenhaDTO();
        alterarSenhaDTO.setEmail("fulano@zup.com.br");
        alterarSenhaDTO.setSenha("AC@432ab");

        informarEmailDTO = new InformarEmailDTO();
        informarEmailDTO.setEmail("fulano@zup.com.br");

        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(usuario);

        pageable = PageRequest.of(0, usuarios.size());

        pageUsuario = new PageImpl(usuarios, pageable, usuarios.size());

    }

    private ResultActions realizarRequisicao(Object object, int statusEsperado, String httpVerbo, String complemento) throws Exception {
        String json = objectMapper.writeValueAsString(object);
        URI uri = UriComponentsBuilder.fromPath("/usuario" + complemento).build().toUri();

        return mockMvc.perform(MockMvcRequestBuilders.request(httpVerbo, uri)
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(statusEsperado));
    }

    @Test
    public void testarCadastroUsuario() throws Exception {
        Mockito.when(usuarioService.cadastrarUsuario(Mockito.any())).thenReturn(usuario);
        String json = objectMapper.writeValueAsString(cadastroUsuarioDTO);

        ResultActions resultadoEsperado = realizarRequisicao(usuario, 201, "POST", "");
        String jsonResposta = resultadoEsperado.andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testarCadastroAdmin() throws Exception {
        Mockito.when(usuarioService.cadastrarAdmin(Mockito.any())).thenReturn(usuario);
        String json = objectMapper.writeValueAsString(cadastroUsuarioDTO);

        ResultActions resultadoEsperado = realizarRequisicao(usuario, 201, "POST", "/cadastraradmin");
        String jsonResposta = resultadoEsperado.andReturn().getResponse().getContentAsString();
    }

    @Test
    @WithMockUser("user@user.com")
    public void testarCadastroValidacaoUsuarioNomeEmBranco() throws Exception {
        Mockito.when(usuarioService.cadastrarUsuario(Mockito.any())).thenReturn(usuario);
        cadastroUsuarioDTO.setNome("    ");
        String json = objectMapper.writeValueAsString(cadastroUsuarioDTO);

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/usuario" + "")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(MockMvcResultMatchers.status().is(422));

    }

    @Test
    @WithMockUser("user@user.com")
    public void testarCadastroValidacaoUsuarioEmailEmBranco() throws Exception {
        Mockito.when(usuarioService.cadastrarUsuario(Mockito.any())).thenReturn(usuario);
        cadastroUsuarioDTO.setEmail("    ");
        String json = objectMapper.writeValueAsString(cadastroUsuarioDTO);

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/usuario" + "")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(MockMvcResultMatchers.status().is(422));

    }

    @Test
    @WithMockUser("user@user.com")
    public void testarCadastroValidacaoUsuarioSenhaEmBranco() throws Exception {
        Mockito.when(usuarioService.cadastrarUsuario(Mockito.any())).thenReturn(usuario);
        cadastroUsuarioDTO.setSenha("  ");
        String json = objectMapper.writeValueAsString(cadastroUsuarioDTO);

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/usuario" + "")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(MockMvcResultMatchers.status().is(422));

    }

    @Test
    @WithMockUser("user@user.com")
    public void testarAlterarSenha() throws Exception {
        Mockito.when(senhaService.alterarSenha(Mockito.any(Usuario.class))).thenReturn(usuario);
        String json = objectMapper.writeValueAsString(alterarSenhaDTO);

        ResultActions resultado = realizarRequisicao(usuario, 200, "PATCH", "/esqueciSenha");
        String jsonResposta = resultado.andReturn().getResponse().getContentAsString();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testarDeletarUsuario() throws Exception {
        Mockito.doNothing().when(usuarioService).deletarUsuario(Mockito.anyString());
        String json = objectMapper.writeValueAsString(informarEmailDTO);

        Assertions.assertEquals(informarEmailDTO.getEmail(), usuario.getEmail());
        ResultActions resultado = realizarRequisicao(usuario, 204, "DELETE", "/deletarUsuario");
        String jsonResposta = resultado.andReturn().getResponse().getContentAsString();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testarDeletarUsuarioNaoExiste() throws Exception {
        Mockito.doThrow(UsuarioNaoExisteException.class).when(usuarioService).deletarUsuario(usuario.getEmail());

        ResultActions resultado = realizarRequisicao(usuario, 404, "DELETE", "/deletarUsuario");
        String jsonResposta = resultado.andReturn().getResponse().getContentAsString();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testarBuscarUsuarios() throws Exception {
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        Mockito.when(usuarioService.buscarUsuarios(pageableCaptor.capture())).thenReturn(pageUsuario);
        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.get("/usuario")
                        .header("Authorization", "admin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));


        PageRequest pageable = (PageRequest) pageableCaptor.getValue();
        Assertions.assertNotNull(pageable);
        Assertions.assertEquals(pageable.getPageSize(), 20);
        Assertions.assertEquals(pageable.getPageNumber(), 0);

    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", ""})
    public void testarBuscarUsuariosRoleInvalido() throws Exception {
        Mockito.doThrow(PerfilInvalidoException.class).when(usuarioService).deletarUsuario(usuario.getEmail());

        ResultActions resultado = realizarRequisicao(usuario, 403, "GET", "");
        String jsonResposta = resultado.andReturn().getResponse().getContentAsString();
    }

}
