<?php

namespace Iccs\CvsanalyDbBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Iccs\CvsanalyDbBundle\Entity\TagRevisions
 */
class TagRevisions
{
    /**
     * @var integer $id
     */
    private $id;

    /**
     * @var integer $tagId
     */
    private $tagId;

    /**
     * @var integer $commitId
     */
    private $commitId;


    /**
     * Get id
     *
     * @return integer 
     */
    public function getId()
    {
        return $this->id;
    }

    /**
     * Set tagId
     *
     * @param integer $tagId
     */
    public function setTagId($tagId)
    {
        $this->tagId = $tagId;
    }

    /**
     * Get tagId
     *
     * @return integer 
     */
    public function getTagId()
    {
        return $this->tagId;
    }

    /**
     * Set commitId
     *
     * @param integer $commitId
     */
    public function setCommitId($commitId)
    {
        $this->commitId = $commitId;
    }

    /**
     * Get commitId
     *
     * @return integer 
     */
    public function getCommitId()
    {
        return $this->commitId;
    }
}