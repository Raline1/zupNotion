package br.com.zup.ZupNotion.models.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data

public class CadastroUsuarioDTO {

    @NotBlank(message = "{validacao.not-blank}")
    @Size(min = 2, message = "{validacao.valor-minimo}")
    private String nome;
    private String sobrenome;
    @NotBlank(message = "{validacao.not-blank}")
    private String email;
    @NotBlank(message = "{validacao.not-blank}")
    @Pattern(regexp ="^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9])(?=.*[a-z]).{8}$",
            message="{validacao.senha-forte}")
    private String senha;
    @NotBlank(message = "{validacao.not-blank}")
    private String perguntaDeSeguranca;
    @NotBlank(message = "{validacao.not-blank}")
    private String respostaDeSeguranca;

}
