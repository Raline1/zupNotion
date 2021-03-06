package br.com.zup.ZupNotion.models.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginDTO {

    @NotBlank(message = "{validacao.not-blank}")
    private String email;
    @NotBlank(message = "{validacao.not-blank}")
    private String senha;

}
