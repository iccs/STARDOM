<?php

namespace Iccs\CvsanalyDbBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Iccs\CvsanalyDbBundle\Entity\Files
 */
class Files
{
    /**
     * @var integer $id
     */
    private $id;

    /**
     * @var string $fileName
     */
    private $fileName;

    /**
     * @var integer $repositoryId
     */
    private $repositoryId;


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
     * Set fileName
     *
     * @param string $fileName
     */
    public function setFileName($fileName)
    {
        $this->fileName = $fileName;
    }

    /**
     * Get fileName
     *
     * @return string 
     */
    public function getFileName()
    {
        return $this->fileName;
    }

    /**
     * Set repositoryId
     *
     * @param integer $repositoryId
     */
    public function setRepositoryId($repositoryId)
    {
        $this->repositoryId = $repositoryId;
    }

    /**
     * Get repositoryId
     *
     * @return integer 
     */
    public function getRepositoryId()
    {
        return $this->repositoryId;
    }
}