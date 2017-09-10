Interface do Chat
Ao ser executado, o chat perguntaria o nome do usuário do mesmo. Exemplo:

User:

Com isso, o usuário digita o seu nome de usuário. Exemplo:

User: tarcisiorocha

Com o nome do usuário, o chat cria a fila do usuário no RabbitMQ e exibe um prompt para que o usuário inicie a comunicação. Exemplo de prompt:

>>

No prompt, se o usuário (tarcisiorocha) quer conversar com uma outro usuário do chat, ele deve digitar "@" seguido do nome do usuário com o qual ele quer conversar. Exemplo:

>> @joaosantos

Com isso, o prompt é alterado automaticamente para exibir o nome do outro usuário para o qual se quer enviar mensagem. Exemplo:

joaosantos>> 

Nesse exemplo, toda nova mensagem digitada no prompt é enviada para "joaosantos" até que o usuário mude para para um novo destinatário. Exemplo:

joaosantos>> Olá, João!!!

joaosantos>> Vamos adiantar o trabalho de SD?

Por exemplo, se o usuário quiser enviar mensagens para outro usuário diferente de "joaosantos", ele deve informar o nome do outro usuário para o qual ele quer enviar mensagem:

joaosantos>> @marciocosta

O comando acima faria o prompt ser "chaveado" para "marciocosta". Com isso, as próximas mensagens seriam enviadas para o usuário "marciocosta":

marciocosta>> Oi, Marcio!!

marciocosta>> Vamos sair hoje?

marciocosta>> Já estou em casa!

marciocosta>>

A qualquer momento, o usuário (exemplo: tarcisiorocha) pode receber mensagem de qualquer outro usuário (marciocosta, joaosantos...). Nesse caso, a mensagem seria impressa na tela da seguinte forma:

(21/09/2016 às 20:53) marciocosta diz: E aí, Tarcisio! Vamos sim!

Depois de impressa a mensagem, o prompt volta para o estado anterior:

marciocosta>> 

Agora segue um exemplo de três mensagens recebidas de joaosantos:

(21/09/2016 às 20:55) joaosantos diz: Opa!

(21/09/2016 às 20:55) joaosantos diz: vamos!!!

(21/09/2016 às 20:56) joaosantos diz: estou indo para a sua casa

Juntamente com a mensagem, são enviados a data e hora de envio e o nome do emissor para que sejam exibidos no console do receptor conforme o exemplo acima. Em suma, cada mensagem enviada deve incluir: nome do emissor, data de envio, hora de envio e o conteúdo da mensagem (texto que o emissor esta enviando ao receptor). O marshalling dos dados que compõem uma mensagem deve ser feito através do uso de Protocol Buffers (https://developers.google.com/protocol-buffers/). Segue o modelo de mensagem sugerido:

message Mensagem{
required string sender = 1; // Nome do emissor
required string date = 2; // Data de envio
required string time = 3; // Hora de envio
optional string group = 4; // Informa o nome do grupo, se a mensagem for para um grupo
message Conteudo{
required string type = 1; // Tipo do conteúdo da mensagem de acordo com o padrão de tipos MIME composto por "tipo/subtipo", exemplos: "text/plain", "text/html", "image/png" (ver: https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Basico_sobre_HTTP/MIME_types)
required bytes body = 2; // Sequência de bytes que compõe o corpo da mensagem
optional string name = 3; // Nome do conteúdo, se existente. Exemplos: "logo_ufs.png", "index.html"
}
repeated Conteudo content = 5;
}

O chat tambem deve disponibilizar comandos de criação de grupos e de adição de membros a um grupo.

Para criar um novo grupo, o usuario pode digitar na linha de comando do chat o simbolo "!" seguido do nome do comando "addGroup" seguido do nome do grupo que se deseja criar. Exemplo de criacao de um grupo chamado "amigos":

marciocosta>> !addGroup amigos

marciocosta>>

Apesar, de nesse exemplo anterior, o usuário estar em uma seção de envio de mensagens para "marciocosta", o chat será capaz de identificar que a entrada "!addGroup amigos" não se trata de uma mensagem a ser enviada ao usuário "marciocosta" e sim um comando ao chat, pelo fato de se iniciar com o simbolo "!". Toda entrada iniciada com "!" deve ser tratada pelo chat como um comando. (Conforme apresentado em aula, a criacao de um grupo no chat deve ser refletir no RabbitMQ como a criacao de um exchange do tipo fanout)

Para incluir um usuário em um grupo deve-se usar o comando "addUser" seguido dos parametros nome do usuario e nome do grupo. No RabbitMQ, incluir um usuário em um grupo deve correponder a associar uma fila a um exchange usando um metodo de bind. Exemplo onde se adiciona os usuários "marciocosta" e "joaosantos" ao grupo amigos:

marciocosta>> !addUser joaosantos amigos

marciocosta>> !addUser marciocosta amigos

marciocosta>>

Assuma também que o usuário que pede para ciar um grupo é adicionado automaticamente ao mesmo grupo. Por exemplo, se considerarmos que foi o usuário "tarcisiorocha" que criou o grupo "amigos", "tarcisiorocha" é adicionado  automaticamente ao grupo amigos (com isso, se tarcisiorocha criou o grupo amigos e adicionou marciocosta e jaosantos, esse grupo fica com tres membros: tarcisiorocha, marciocosta e joaosantos).

No prompt, se o usuário (tarcisiorocha) quer enviar mensagem para um grupo, ele deve digitar "#" seguido do nome do grupo para o qual ele quer enviar mensagens. Depois que o usuário pressionar a tecla <ENTER>, o prompt é alterado para exibir o nome do grupo correspondente e a indicação entre parêntesis de que se trata de um grupo. Exemplo:

marciocosta>> #amigos

amigos*>>  

A partir disso, o usuário poderá digitar as mensagens para o respectivo grupo:

amigos*>> Olá, pessoal!

amigos*>> Alguém vai ao show de Djavan?

amigos*>>

O modelo de mensagem Protocol Buffers interno para o envio de mensagens para um grupo é o mesmo que mostrado anteriormente para o envio de mensagens individuais, com a exceção de que como conteúdo da tag "sender", vai o nome do grupo e o nome do emissor separados por uma "/". Exemplo de mensagem enviada pelo usuário tarcisiorocha dentro do contexto do grupo "amigos":

Com esse modelo de mensagem unificado, ao receber uma mensagem de um usuário dentro do contexto de um grupo, o receptor saberá quem foi que enviou e dentro o contexto de que grupo.

No RabbitMQ o envio de uma mensagem para um grupo deve corresponder ao envio de uma única mensagem ao exchange correspondente que, por sua vez, será o responsável por replicar a mensagem nas diversas filas dos integrantes do respectivo grupo.

Devem ser incluidos também comandos para excluir um grupo e remover um usuário do grupo.Para remover um usuário de um determinado grupo, deve-se diponibilizar o comando "delUser" seguido do <nome do usuário> e do <nome do grupo>. Exemplo:

marciocosta>> !delUser joaosantos amigos

marciocosta>>

Neste último exemplo, joaosantos é removido do grupo amigos.

Para excluir um grupo, deve-se adotar o comando "delGroup" seguido do <nome do grupo> a ser removido. Exemplo:

marciocosta> !delGroup amigos

marciocosta>

O efeito do comando "delGroup" deve ser refletido no RabbitMQ como a exclusão do respectivo exchange.
