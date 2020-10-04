<html>
    <body>
        <div>${displayName}</div>
        <ul>
            <#list phrases as phrase>
                <li>${phrase}</li>
            </#list>
        </ul>
        <form method="post" action="/phrases">
            Emoji:<br>
            <input type="text" name="emoji" /><br>
            Phrase:<br>
            <input type="text" name="phrase" /><br>
            <input type="submit" name="Submit" /><br>
        </form>
    </body>
</html>