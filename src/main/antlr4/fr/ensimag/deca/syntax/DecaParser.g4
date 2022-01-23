parser grammar DecaParser;

options {
    // Default language but name it anyway
    //
    language  = Java;

    // Use a superclass to implement all helper
    // methods, instance variables and overrides
    // of ANTLR default methods, such as error
    // handling.
    //
    superClass = AbstractDecaParser;

    // Use the vocabulary generated by the accompanying
    // lexer. Maven knows how to work out the relationship
    // between the lexer and parser and will build the
    // lexer before the parser. It will also rebuild the
    // parser if the lexer changes.
    //
    tokenVocab = DecaLexer;

}

@header {
    import fr.ensimag.deca.tree.*;
    import java.io.PrintStream;
}

@members {
    @Override
    protected AbstractProgram parseProgram() {
        return prog().tree;
    }
}

prog returns[AbstractProgram tree]
    : list_classes main EOF {
            assert($list_classes.tree != null);
            assert($main.tree != null);
            $tree = new Program($list_classes.tree, $main.tree);
            setLocation($tree, $list_classes.start);
        }
    ;

main returns[AbstractMain tree]
    : /* epsilon */ {
            $tree = new EmptyMain();
        }
    | block {
            assert($block.decls != null);
            assert($block.insts != null);
            $tree = new Main($block.decls, $block.insts);
            setLocation($tree, $block.start);
        }
    ;

block returns[ListDeclVar decls, ListInst insts]
    : OBRACE list_decl list_inst CBRACE {
            assert($list_decl.tree != null);
            assert($list_inst.tree != null);
            $decls = $list_decl.tree;
            $insts = $list_inst.tree;
        }
    ;

list_decl returns[ListDeclVar tree]
@init   {
            $tree = new ListDeclVar();
        }
    : decl_var_set[$tree]*
    ;

decl_var_set[ListDeclVar l]
    : type list_decl_var[$l,$type.tree] SEMI
    ;

list_decl_var[ListDeclVar l, AbstractIdentifier t]
    : dv1=decl_var[$t] {
        $l.add($dv1.tree);
        } (COMMA dv2=decl_var[$t] {
            $l.add($dv2.tree);
        }
      )*
    ;

decl_var[AbstractIdentifier t] returns[AbstractDeclVar tree]
@init   {
            AbstractIdentifier ident;
            AbstractInitialization init=null;
        }
    : i=ident {
            ident=$i.tree;
            setLocation(ident, $i.start);
        }
      (EQUALS e=expr {
            init= new Initialization($e.tree); //on initialise la variable
            setLocation(init, $EQUALS);
        }
      )? {
            if (init == null) { //la variable n'a pas été initialisée
                      init = new NoInitialization();
            }
            $tree = new DeclVar($t, ident, init); //dans tous les cas notre variable est déclarée
            $tree.setLocation(ident.getLocation());

        }
    ;

list_inst returns[ListInst tree]
@init {
    $tree = new ListInst();
}
    : (inst {
        $tree.add($inst.tree);
        }
      )*
    ;

inst returns[AbstractInst tree]
    : e1=expr SEMI {
            assert($e1.tree != null);
            $tree=$e1.tree;
            setLocation($tree, $e1.start);
        }
    | SEMI {
            $tree=new NoOperation(); //il y a rien
            setLocation($tree, $SEMI);
        }
    | PRINT OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null); //tjs tester si c'est pas null
            $tree = new Print(false, $list_expr.tree); //false car on veut pas du hexa
            setLocation($tree, $PRINT);

        }//TOBETESTED
    | PRINTLN OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree= new Println(false, $list_expr.tree); //pareil false car pas en hexa
            setLocation($tree, $PRINTLN);
        }
    | PRINTX OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Print(true, $list_expr.tree); //true car on veut de l'hexa
            setLocation($tree, $PRINTX);
        }//TOBETESTED
    | PRINTLNX OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Println(true, $list_expr.tree);
            setLocation($tree, $PRINTLNX);
        } //TOBETESTED
    | if_then_else {
            assert($if_then_else.tree != null);
            $tree=$if_then_else.tree; //on va vers le if_then_else
            setLocation($tree, $if_then_else.start);
        } //TOBETESTED
    | WHILE OPARENT condition=expr CPARENT OBRACE body=list_inst CBRACE { //si on a un while
            assert($condition.tree != null);
            assert($body.tree != null);
            $tree= new While($condition.tree, $body.tree);
            setLocation($tree, $WHILE);
        } //TOBETESTED
    | RETURN expr SEMI { //si c'est un return
            assert($expr.tree != null);
            $tree= new Return($expr.tree);
            setLocation($tree, $RETURN);
        }
    ;

//TOBETESTED
if_then_else returns[IfThenElse tree]
@init {
    IfThenElse lastOne = null; //correspond au dernier if fait
}
    : if1=IF OPARENT condition=expr CPARENT OBRACE li_if=list_inst CBRACE {
        $tree = new IfThenElse($condition.tree, $li_if.tree, new ListInst()); //on a pas le else donc on créé une nouvelle ListInst()
        setLocation($tree, $if1);
        lastOne=$tree; //on met à jour le dernier if
        }
      (ELSE elsif=IF OPARENT elsif_cond=expr CPARENT OBRACE elsif_li=list_inst CBRACE {
            IfThenElse elsif = new IfThenElse($elsif_cond.tree, $elsif_li.tree, new ListInst()); //pareil on a pas la suite du else
            setLocation(elsif, $elsif);
            lastOne.setElseBranch(elsif);
            lastOne=elsif; //on met à jour
        }
      )*
      (ELSE OBRACE li_else=list_inst CBRACE {
            lastOne.setElseBranch($li_else.tree); //comme on a enfin le else on peut set la branche du else
        }
      )?
    ;


list_expr returns[ListExpr tree]
@init   {
    $tree= new ListExpr();
        }
    : (e1=expr {
            $tree.add($e1.tree);
            setLocation($tree, $e1.start);
        }
       (COMMA e2=expr {
            $tree.add($e2.tree);
            setLocation($tree, $e2.start);
        }
       )* )?
    ;


expr returns[AbstractExpr tree]
    : assign_expr {
            assert($assign_expr.tree != null);
            $tree=$assign_expr.tree;
            setLocation($tree, $assign_expr.start);
        }
    ;


assign_expr returns[AbstractExpr tree]
    : e=or_expr (
        /* condition: expression e must be a "LVALUE" */ {
            if (! ($e.tree instanceof AbstractLValue)) {
                throw new InvalidLValue(this, $ctx);
            }
        }
        EQUALS e2=assign_expr { //on doit assign e1=e2
            assert($e.tree != null);
            assert($e2.tree != null); //on vérifie le non null
            $tree = new Assign((AbstractLValue)$e.tree, $e2.tree);
            setLocation($tree, $EQUALS);

        } //TOBETESTED
      | /* epsilon */ {
            assert($e.tree != null);
            $tree=$e.tree; //si il y a pas d'assignation on fait rien
            setLocation($tree, $e.start);
        }
      )
    ;


or_expr returns[AbstractExpr tree]
    : e=and_expr {
            assert($e.tree != null);
            $tree=$e.tree; //si il y a pas de OR on fait rien de particulier et on continue
            setLocation($tree, $e.start);
       }
    | e1=or_expr OR e2=and_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree= new Or($e1.tree, $e2.tree); //on créé un nouveau Or qu'on set après
            setLocation($tree, $OR);
       }//TOBETESTED
    ;


and_expr returns[AbstractExpr tree]
    : e=eq_neq_expr {
            assert($e.tree != null);
            $tree=$e.tree; //Si pas de And on fait rien de particulier et on continue
            setLocation($tree, $e.start);
        }
    |  e1=and_expr AND e2=eq_neq_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new And($e1.tree, $e2.tree); //on créé donc le nouveau And qu'on set après
            setLocation($tree, $AND);
        }//TOBETESTED
    ;


eq_neq_expr returns[AbstractExpr tree]
    : e=inequality_expr {
            assert($e.tree != null);
            $tree=$e.tree; //si il y a rien on continue
            setLocation($tree, $e.start);
        }
    | e1=eq_neq_expr EQEQ e2=inequality_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Equals($e1.tree, $e2.tree); //nouveau Equals qu'on set après
            setLocation($tree, $EQEQ);
        }//TOBETESTED
    | e1=eq_neq_expr NEQ e2=inequality_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new NotEquals($e1.tree, $e2.tree); //nouveau NotEquals qu'on set après
            setLocation($tree, $NEQ);
        }//TOBETESTED
    ;


inequality_expr returns[AbstractExpr tree]
    : e=sum_expr {
            assert($e.tree != null);
            $tree=$e.tree; //si rien de particulier on continue et on fait rien
            setLocation($tree, $e.start);
        }
    | e1=inequality_expr LEQ e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new LowerOrEqual($e1.tree, $e2.tree); //on créé un nouveau tree qu'on set après
            setLocation($tree, $LEQ);
        }//TOBETESTED
    | e1=inequality_expr GEQ e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new GreaterOrEqual($e1.tree, $e2.tree); //on créé un nouveau tree qu'on set après
            setLocation($tree, $GEQ);
        }//TOBETESTED
    | e1=inequality_expr GT e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Greater($e1.tree, $e2.tree); //on créé un nouveau tree qu'on set après
            setLocation($tree, $GT);
        }//TOBETESTED
    | e1=inequality_expr LT e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Lower($e1.tree, $e2.tree); //on créé un nouveau tree qu'on set après
            setLocation($tree, $LT);
        }//TOBETESTED
    | e1=inequality_expr INSTANCEOF type {
            assert($e1.tree != null);
            assert($type.tree != null);
            $tree = new InstanceOf($e1.tree, $type.tree); //on créé un nouveau tree qu'on set après
            setLocation($tree, $INSTANCEOF);
        }//TOBETESTED
    ;



sum_expr returns[AbstractExpr tree]
    : e=mult_expr {
            assert($e.tree != null);
            $tree=$e.tree; //si pas d'adition ou de soustraction on fait rien et on continue
            setLocation($tree, $e.start);
        }
    | e1=sum_expr PLUS e2=mult_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree=new Plus($e1.tree, $e2.tree); //on créé un nouveau tree qu'on set après
            setLocation($tree, $PLUS);
        }
    | e1=sum_expr MINUS e2=mult_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree=new Minus($e1.tree, $e2.tree); //on créé un nouveau tree qu'on set après
            setLocation($tree, $MINUS);
        }
    ;


mult_expr returns[AbstractExpr tree]
    : e=unary_expr {
            assert($e.tree != null);
            $tree=$e.tree; //si pas de multiplication ou autre on fait rien et on continue
            setLocation($tree, $e.start);
        }
    | e1=mult_expr TIMES e2=unary_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree=new Multiply($e1.tree, $e2.tree); //on créé un nouveau tree qu'on set après
            setLocation($tree, $TIMES);
        }
    | e1=mult_expr SLASH e2=unary_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree=new Divide($e1.tree, $e2.tree); //on créé un nouveau tree qu'on set après
            setLocation($tree, $SLASH);
        }
    | e1=mult_expr PERCENT e2=unary_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree=new Modulo($e1.tree, $e2.tree); //on créé un nouveau tree qu'on set après
            setLocation($tree, $PERCENT);
        }
    ;


unary_expr returns[AbstractExpr tree]
    : op=MINUS e=unary_expr {
            assert($e.tree != null);
            $tree=new UnaryMinus($e.tree); //on créé un nouveau tree qu'on set après
            setLocation($tree, $op);
        }
    | op=EXCLAM e=unary_expr {
            assert($e.tree != null);
            $tree=new Not($e.tree); //on créé un nouveau tree qu'on set après
            setLocation($tree, $op);
        }
    | select_expr {
            assert($select_expr.tree != null);
            $tree=$select_expr.tree; //si pas d'unary opération on fait rien et on continue normalement
            setLocation($tree, $select_expr.start);
        }
    ;


select_expr returns[AbstractExpr tree]
    : e=primary_expr {
            assert($e.tree != null);
            $tree=$e.tree; //si il y a rien on continue normalement
            setLocation($tree, $e.start);
        }
    | e1=select_expr DOT i=ident {
            assert($e1.tree != null);
            assert($i.tree != null);
        }//TODO gestion des erreurs ?
        (o=OPARENT args=list_expr CPARENT {
            // we matched "e1.i(args)"
            // i correspond au nom de la méthode qu'on va appeler
            assert($args.tree != null);
            $tree = new MethodCall($e1.tree, $i.tree, $args.tree); //on appelle une methode déjà créee
            setLocation($tree, $o);
        }//TOBETESTED
        | /* epsilon */ {
            // we matched "e.i"
            $tree = new Selection($e1.tree, $i.tree);;
            setLocation($tree, $DOT);
        }//TOBETESTED
        )
    ;


primary_expr returns[AbstractExpr tree]
    : ident {
            assert($ident.tree != null);
            $tree=$ident.tree;
            setLocation($tree, $ident.start);
        }
    | m=ident OPARENT args=list_expr CPARENT {
            assert($args.tree != null);
            assert($m.tree != null);
            This t = new This(true); // m() signifie this.m()
            t.setLocation($m.tree.getLocation());

            $tree = new MethodCall(t, $m.tree, $args.tree);
            //encore une fois on appelle une méthode mais sans vraiment de paramètres implicites
            // donc on fait this().m(args) avec m le nom de la méthode
            setLocation($tree, $OPARENT);
        }//TOBETESTED
    | OPARENT expr CPARENT {
            assert($expr.tree != null);
            $tree=$expr.tree;
        }
    | READINT OPARENT CPARENT {
            $tree = new ReadInt();
            setLocation($tree, $READINT);
        }//TOBETESTED
    | READFLOAT OPARENT CPARENT {
            $tree = new ReadFloat();
            setLocation($tree, $READFLOAT);
        }//TOBETESTED
    | NEW ident OPARENT CPARENT {
            assert($ident.tree != null);
            $tree = new New($ident.tree);
            setLocation($tree, $NEW);
        }//TOBETESTED
    | cast=OPARENT type CPARENT OPARENT expr CPARENT {
            assert($type.tree != null);
            assert($expr.tree != null);
            //ici on change le type de expr avec un cast
            $tree = new Cast($type.tree, $expr.tree);
            setLocation($tree, $cast);

        }//TOBETESTED
    | literal {
            assert($literal.tree != null);
            $tree=$literal.tree;
            setLocation($tree, $literal.start);
        }
    ;


type returns[AbstractIdentifier tree]
    : ident {
            assert($ident.tree != null);
            $tree = $ident.tree;
            setLocation($tree, $ident.start);
        }
    ;


literal returns[AbstractExpr tree]
    : INT {
        $tree = new IntLiteral(Integer.parseInt($INT.getText()));
        setLocation($tree, $INT);
        }//TOBETESTED
    | fd=FLOAT {
        $tree = new FloatLiteral(Float.parseFloat($fd.text));
        setLocation($tree, $fd);
        }//TOBETESTED
    | STRING
        //Pour ne pas afficher les guillemets dans un String il faut le filtrer, le parcourir
        //et ne garder que le contenu du String
        //Pour éviter les guillemets on part à i=1 et on fini a length-1
        {
        StringBuffer nouveau = new StringBuffer(); //correspond à notre nouveau String auquel ou ajoute le contenu
        String s=$STRING.getText();

        for (int i=1; i<s.length()-1; i++){

            if (s.charAt(i)=='\n' || s.charAt(i)=='\\'){
                //on ne veut pas ajouter ces symboles donc on les passe et on ajoute le suivant
                i++;
            }
            nouveau.append(s.charAt(i));
        }
        $tree = new StringLiteral(nouveau.toString());
        setLocation($tree, $STRING);
        }
    | TRUE {
        $tree = new BooleanLiteral(true);
        setLocation($tree, $TRUE);
        }//TOBETESTED
    | FALSE {
        $tree = new BooleanLiteral(false);
        setLocation($tree, $FALSE);
        }//TOBETESTED
    | THIS {
        $tree = new This(true);
        setLocation($tree, $THIS);
        }//TOBETESTED
    | NULL {
        $tree = new Null();
        setLocation($tree, $NULL);
        }//TOBETESTED
    ;


ident returns[AbstractIdentifier tree]
    : IDENT {
        $tree = new Identifier(getDecacCompiler().getSymbolTable().create($IDENT.getText()));
        //on créé un nouveau identifier en créant un nouveau symbol
        setLocation($tree, $IDENT);
        }//TOBETESTED
    ;

/****     Class related rules     ****/


list_classes returns[ListDeclClass tree]
@init{
    $tree = new ListDeclClass();
}    :
      (c1=class_decl {
            assert($c1.tree !=null);
            $tree.add($c1.tree); //on ajoute toutes les déclaration de classes qu'on fait
        }
      )*
    ;

class_decl returns[DeclClass tree]
    : CLASS name=ident superclass=class_extension OBRACE class_body CBRACE {
            //chaque déclaration de classe comprend son nom, sa classe mère, ses attributs et ses méthodes
            $tree = new DeclClass($name.tree, $superclass.tree, $class_body.field, $class_body.method);
            setLocation($tree, $name.start);
            setLocation($superclass.tree, $superclass.start);
            setLocation($name.tree, $name.start);
        }
    ;

class_extension returns[AbstractIdentifier tree]
    : EXTENDS ident {
            $tree=$ident.tree;
            setLocation($ident.tree, $ident.start);
        }
    | /* epsilon */ {
            $tree = new Identifier(getDecacCompiler().getSymbolTable().create("Object"));
            $tree.setLocation(Location.BUILTIN);
        }
    ;

class_body returns[ListDeclField field, ListDeclMethod method]
@init{
 //dans une classe on a des methodes et des attributs donc on les ajoute
 $method = new ListDeclMethod();
 $field = new ListDeclField();
}
    : (m=decl_method {
            $method.add($m.tree); //on ajoute à notre liste des methodes déclarées chaque méthode
        }
      | decl_field_set [$field]

      )*
    ;

decl_field_set [ListDeclField tree]
    : v=visibility t=type list_decl_field[$tree, $v.tree, $t.tree]
      SEMI
    ;

visibility returns[Visibility tree] //visibilité des attributs qui sont soit public soit protected
    : /* epsilon */ {
            $tree = Visibility.PUBLIC;
        }
    | PROTECTED {
            $tree = Visibility.PROTECTED;
        }
    ;

list_decl_field [ListDeclField tree, Visibility v, AbstractIdentifier iden]
    : dv1=decl_field [$v, $iden]
    {
        $tree.add($dv1.tree); //on ajoute tous les attributs déclarés
    }
        (COMMA dv2=decl_field [$v, $iden]
    {
        $tree.add($dv2.tree);
    }
      )*
    ;

decl_field [Visibility v, AbstractIdentifier t] returns[AbstractDeclField tree]
@init{
    AbstractIdentifier ident; //nom de l'attribut
    AbstractInitialization init = null; //si l'attribut est initialisé en même temps que sa déclaration
}
    : i=ident {
            ident = $i.tree;
        }
      (EQUALS e=expr {
            init = new Initialization($e.tree); //on a une initialisation
            setLocation(init, $EQUALS);
        }
      )? {
            if (init == null) { //l'attribut n'a pas été initialisé
                init = new NoInitialization();
            }
            $tree = new DeclField($v, $t, ident, init); //dans tous les cas on déclare l'attribut
            $tree.setLocation(ident.getLocation());
        }
    ;


decl_method returns[DeclMethod tree]
@init {
    AbstractMethodBody body = null; //au départ il y a rien dans la méthode
}
    : type ident OPARENT params=list_params CPARENT (block {
            //on ajoute dans notre méthode toutes les déclaration et instructions de block
            body = new MethodBody($block.decls, $block.insts);
            setLocation(body, $block.start);
        }
      | ASM OPARENT code=multi_line_string CPARENT SEMI {
            //methode écrite en assembleur aucune vérification du code est faite on récupère le code tel quel
            String s = $code.text;
            StringBuffer f = new StringBuffer();
            for (int i = 1; i < s.length() - 1; i++) {
                    if (s.charAt(i) == '\\') {
                            i++;
                    }
                    f.append(s.charAt(i));
            }
            AbstractStringLiteral asmCode = new StringLiteral(f.toString());
            asmCode.setLocation($code.location);
            body = new MethodAsmBody(asmCode);
            setLocation(body, $ASM);
        }
      ) {
            $tree = new DeclMethod($type.tree, $ident.tree, $params.tree, body);
            setLocation($tree, $type.start);
        }
    ;

list_params returns[ListDeclParam tree]
@init{
    $tree = new ListDeclParam();
}
    : (p1=param {
            $tree.add($p1.tree); //on ajoute les paramètres à la liste
        } (COMMA p2=param {
            $tree.add($p2.tree);
        }
      )*)?
    ;

multi_line_string returns[String text, Location location]
    : s=STRING {
            $text = $s.text;
            $location = tokenLocation($s);
        }
    | s=MULTI_LINE_STRING {
            $text = $s.text;
            $location = tokenLocation($s);
        }
    ;

param returns[AbstractDeclParam tree]
    : type ident {
            $tree = new DeclParam($type.tree, $ident.tree); //on déclare les paramètres
            setLocation($tree, $type.start);
        }
    ;
