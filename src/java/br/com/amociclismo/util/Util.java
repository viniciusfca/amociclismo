/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.amociclismo.util;

import br.com.amociclismo.dao.UsuarioDAO;
import br.com.amociclismo.entity.Usuario;
import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.mail.HtmlEmail;
import org.apache.tomcat.util.codec.binary.Base64;

/**
 *
 * @author Vinicius
 */
public class Util {

    /**
     * Metodo que retira pontos e traços de uma String
     *
     * @param valor
     * @return
     */
    public static String retirarPontos(String valor) {
        valor = valor.replace("-", "").replace("/", "").replace("(", "").replace(")", "").replace(".", "");
        return valor;
    }

    public static void saveMessage(String msg, String alert) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(msg, alert));

    }

    /**
     * Metodo que remove usuario da sessao
     */
    public static void retirarUsuarioSessao() {
        HttpSession ses = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        ses.invalidate();
    }

    /**
     * Metodo que coloca o usuario na sessao
     *
     * @param usuario
     */
    public static void colocarUsuarioSessao(Usuario usuario) {
        HttpSession ses = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        ses.setAttribute("usuarioLogado", usuario);
    }

    /**
     * Método que converte a Date util para Date sql
     *
     * @param myDate
     * @return
     */
    public static java.sql.Date tratarData(java.util.Date myDate) {
        java.util.Calendar cal = Calendar.getInstance();

        cal.setTime(myDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        // adiciono um dia, porque por algum motivo ele volta um dia
        //cal.add(Calendar.DAY_OF_MONTH, 1);
        java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());

        return sqlDate;
    }

    /**
     * Metodo que redireciona pagina
     *
     * @param url
     */
    public static void redirecionar(String url) {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(url);
        } catch (IOException ex) {
            saveMessage("Erro ao Redirecionar", ex.getMessage());
        }
    }

    /**
     * Metodo que encriptografa texto
     *
     * @param value
     * @return
     */
    public static String encrypt(String value) {
        try {
            value = Base64.encodeBase64String(value.getBytes());
            return value;
        } catch (Exception ex) {
            System.out.println("Erro ao criptografar: " + ex);
        }
        return null;
    }

    /**
     * Metodo que descriptografa
     *
     * @param value
     * @return
     */
    public static String decrypt(String value) {
        try {

            byte[] decoded = Base64.decodeBase64(value);
            String retorno = new String(decoded);

            return retorno;

        } catch (Exception e) {
            System.out.println("Erro ao decriptografar: " + e.getMessage());
        }

        return null;
    }

    /**
     * Metodo que valida email
     *
     * @param email
     * @return
     */
    public static boolean isEmailValido(String email) {
        if ((email == null) || (email.trim().length() == 0)) {
            return false;
        }
        String emailPattern = "\\b(^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b";
        Pattern pattern = Pattern.compile(emailPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String StringPrimeiraLetraMaiuscula(String str) {

        if (!str.isEmpty()) {
            return str.substring(0, 1).toUpperCase().concat(str.substring(1));
        }

        return "";
    }

    /**
     * METODO VALIDA O CPF
     *
     * @param pCPF
     * @return
     */
    public static boolean isCPF(String pCPF) {
        String CPF = pCPF.replace(".", "").replace("-", "").trim();
        // considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (CPF.equals("00000000000") || CPF.equals("11111111111")
                || CPF.equals("22222222222") || CPF.equals("33333333333")
                || CPF.equals("44444444444") || CPF.equals("55555555555")
                || CPF.equals("66666666666") || CPF.equals("77777777777")
                || CPF.equals("88888888888") || CPF.equals("99999999999")
                || (CPF.length() != 11)) {
            return (false);
        }

        char dig10, dig11;
        int sm, i, r, num, peso;

        // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                // converte o i-esimo caractere do CPF em um numero:
                // por exemplo, transforma o caractere '0' no inteiro 0         
                // (48 eh a posicao de '0' na tabela ASCII)         
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig10 = '0';
            } else {
                dig10 = (char) (r + 48); // converte no respectivo caractere numerico
            }
            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig11 = '0';
            } else {
                dig11 = (char) (r + 48);
            }

            // Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10))) {
                return (true);
            } else {
                return (false);
            }
        } catch (Exception erro) {
            return (false);
        }
    }

    public static String getIp() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ip = null;
        ip = request.getHeader("x-forwarded-for");
        if (ip == null) {
            ip = request.getHeader("X_FORWARDED_FOR");
            if (ip == null) {
                ip = request.getRemoteAddr();
            }
        }
        return ip;
    }

    /**
     * Metodo que retorna o usuario logado
     *
     * @return
     */
    public static Usuario getUsuarioLogado() {
        return (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLogado");
    }

    /**
     * Metodo que envia email
     */
    public static void enviarEmail(Usuario usuario) {

        HtmlEmail email = new HtmlEmail();

        email.setHostName("mail.amociclismo.com.br");
        email.setSmtpPort(587);
        email.setSSLOnConnect(true);
        email.setDebug(true);
        email.setAuthentication("suporte@amociclismo.com.br", "Bike#$AmoBike");

        try {
            email.addTo(usuario.getEmail());
            email.setFrom("suporte@amociclismo.com.br", "AMO CICLISMO");
            email.setSubject("Confirmação de Cadastro - AMO CICLISMO");

            String emailBody = "<html>\n"
                    + "<head>\n"
                    + "<meta http-equiv='Content-Type' content='text/html; charset=utf-8'/>"
                    + "	<title>.::AMO CICLISMO::.</title>\n"
                    + "	<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css' integrity='sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u' crossorigin='anonymous'>\n"
                    + "</head>\n"
                    + "\n"
                    + "	<body>\n"
                    + "		<div class='panel panel-info' style='font-size: 11px;width: 600px;height: 510px;'  >\n"
                    + "                            <div class='panel-heading' >\n"
                    + "                                <b><center>.::AMO CICLISMO::.</b></center>\n"
                    + "                            </div>\n"
                    + "\n"
                    + "                            <div class='panel' style='height: 10px;'>\n"
                    + "								\n"
                    + "                            </div>\n"
                    + "\n"
                    + "                            <div class='panel-body' style='margin-top: -1.3%'>\n"
                    + "                                <h3><center>CADASTRO EFETUADO COM SUCESSO</center></h3>\n"
                    + "								<h4>\n"
                    + "                                    <center><p><b>Olá," + usuario.getNome() + "<b/></p>\n"
                    + "                                    <p>Para confirmar seu cadastro acesse o link abaixo.</p>\n"
                    + "                                    <center><a href='http://www.amociclismo.com.br/amociclismo/index2.jsf'><img src='http://www.bodyfitnesspt.com/wp-content/uploads/2014/04/blue-tick.png' style='width: 100px; height: 100px'/></a></center>\n"
                    + "                                </h4>\n"
                    + "                                \n"
                    + "								<img src='http://www.amociclismo.com.br/Imagens/AmoCiclismo.jpg' style='width: 150px; height: 150px; margin-top: 40px'/>\n"
                    + "								<p style='margin-left: 15px'>Equipe AMO CICLISMO.</p>\n"
                    + "                            </div>\n"
                    + "\n"
                    + "                        </div>\n"
                    + "	\n"
                    + "	\n"
                    + "	</body>\n"
                    + "</html>";

            email.setHtmlMsg(emailBody);
            email.send();

        } catch (Exception e) {
            System.out.println("Erro Falha ao enviar email " + e.getMessage());
        }

    }

    public static String reenviarSenha(String cpf) {

        Usuario usuario = new Usuario();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        String retorno = "";

        usuario = usuarioDAO.getUsuarioByCpf(retirarPontos(cpf));

        if (usuario.getId() > 0) {
            HtmlEmail email = new HtmlEmail();


            email.setHostName("mail.amociclismo.com.br");
            email.setSmtpPort(587);
            email.setSSLOnConnect(true);
            email.setDebug(true);
            email.setAuthentication("suporte@amociclismo.com.br", "Bike#$AmoBike");

            try {
                email.addTo(usuario.getEmail());

                email.setFrom("suporte@amociclismo.com.br", "AMO CICLISMO");
                email.setSubject("Recuperação de Senha - AMO CICLISMO");

                String emailBody = "<html>\n"
                        + "<head>\n"
                        + "<meta http-equiv='Content-Type' content='text/html; charset=utf-8'/>"
                        + "	<title>.::AMO CICLISMO::.</title>\n"
                        + "	<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css' integrity='sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u' crossorigin='anonymous'>\n"
                        + "</head>\n"
                        + "\n"
                        + "	<body>\n"
                        + "		<div class='panel panel-info' style='font-size: 11px;width: 600px;height: 510px;'  >\n"
                        + "                            <div class='panel-heading' >\n"
                        + "                                <b><center>.::AMO CICLISMO::.</b></center>\n"
                        + "                            </div>\n"
                        + "\n"
                        + "                            <div class='panel' style='height: 10px;'>\n"
                        + "								\n"
                        + "                            </div>\n"
                        + "\n"
                        + "                            <div class='panel-body' style='margin-top: -1.3%'>\n"
                        + "                                <h3><center>RECUPERAÇÃO DE SENHA</center></h3>\n"
                        + "								<h4>\n"
                        + "                                    <center><p><b>Olá," + usuario.getNome() + "<b/></p>\n"
                        + "                                    <p>Sua senha é:</p>\n"
                        + "                                    <center><b>" + decrypt(usuario.getSenha()) + "</center>\n"
                        + "                                </h4>\n"
                        + "                                \n"
                        + "								<img src='http://www.amociclismo.com.br/Imagens/AmoCiclismo.jpg' style='width: 150px; height: 150px; margin-top: 40px'/>\n"
                        + "								<p style='margin-left: 15px'>Equipe AMO CICLISMO.</p>\n"
                        + "                            </div>\n"
                        + "\n"
                        + "                        </div>\n"
                        + "	\n"
                        + "	\n"
                        + "	</body>\n"
                        + "</html>";
                
                
                email.setHtmlMsg(emailBody);
                email.send();

                retorno = "Senha reenviada para o e-mail vinculado ao CPF.";

            } catch (Exception e) {
                System.out.println("Erro Falha ao enviar email " + e.getMessage());
            }
        } else {
            retorno = "O CPF informado não está cadastrado na nossa base.";
        }

        return retorno;
    }

}
